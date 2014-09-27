/*******************************************************************************
 * Copyright 2010 Sam Steele 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.c99.SyncProviderDemo;

import android.accounts.Account;
import android.accounts.OperationCanceledException;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.RawContacts.Entity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import recode.appro.conexao.JSONParser;
import recode.appro.noticia.controle.ControladorNoticia;
import recode.appro.noticia.modelo.Noticia;
import recode.appro.main.view.NavigationDrawer;
import recode.appro.telas.R;

/**
 * @author sam
 * 
 */
public class NoticiasSyncAdapterService extends Service {
	private static final String TAG = "NoticiasSyncAdapterService";
	private static SyncAdapterImpl sSyncAdapter = null;
	private static ContentResolver mContentResolver = null;
	private static String UsernameColumn = RawContacts.SYNC1;
	private static String PhotoTimestampColumn = RawContacts.SYNC2;

    // sync variaveis
    // end sync variaveis

	public NoticiasSyncAdapterService() {
		super();
	}

	private static class SyncAdapterImpl extends AbstractThreadedSyncAdapter {
		private Context mContext;

		public SyncAdapterImpl(Context context) {
			super(context, true);
			mContext = context;
		}

		@Override
		public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
			try {
				NoticiasSyncAdapterService.performSync(mContext, account, extras, authority, provider, syncResult);
			} catch (OperationCanceledException e) {
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		IBinder ret = null;
		ret = getSyncAdapter().getSyncAdapterBinder();
		return ret;
	}

	private SyncAdapterImpl getSyncAdapter() {
		if (sSyncAdapter == null)
			sSyncAdapter = new SyncAdapterImpl(this);
		return sSyncAdapter;
	}

	private static void addContact(Account account, String name, String username) {
		Log.i(TAG, "Adding contact: " + name);
		ArrayList<ContentProviderOperation> operationList = new ArrayList<ContentProviderOperation>();

		ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(RawContacts.CONTENT_URI);
		builder.withValue(RawContacts.ACCOUNT_NAME, account.name);
		builder.withValue(RawContacts.ACCOUNT_TYPE, account.type);
		builder.withValue(RawContacts.SYNC1, username);
		operationList.add(builder.build());

		builder = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
		builder.withValueBackReference(ContactsContract.CommonDataKinds.StructuredName.RAW_CONTACT_ID, 0);
		builder.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
		builder.withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name);
		operationList.add(builder.build());

		builder = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
		builder.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0);
		builder.withValue(ContactsContract.Data.MIMETYPE, "vnd.android.cursor.item/vnd.org.c99.SyncProviderDemo.profile");
		builder.withValue(ContactsContract.Data.DATA1, username);
		builder.withValue(ContactsContract.Data.DATA2, "SyncProviderDemo Profile");
		builder.withValue(ContactsContract.Data.DATA3, "View profile");
		operationList.add(builder.build());

		try {
			mContentResolver.applyBatch(ContactsContract.AUTHORITY, operationList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void updateContactStatus(ArrayList<ContentProviderOperation> operationList, long rawContactId, String status) {
		Uri rawContactUri = ContentUris.withAppendedId(RawContacts.CONTENT_URI, rawContactId);
		Uri entityUri = Uri.withAppendedPath(rawContactUri, Entity.CONTENT_DIRECTORY);
		Cursor c = mContentResolver.query(entityUri, new String[] { RawContacts.SOURCE_ID, Entity.DATA_ID, Entity.MIMETYPE, Entity.DATA1 }, null, null, null);
		try {
			while (c.moveToNext()) {
				if (!c.isNull(1)) {
					String mimeType = c.getString(2);

					if (mimeType.equals("vnd.android.cursor.item/vnd.org.c99.SyncProviderDemo.profile")) {
						ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(ContactsContract.StatusUpdates.CONTENT_URI);
						builder.withValue(ContactsContract.StatusUpdates.DATA_ID, c.getLong(1));
						builder.withValue(ContactsContract.StatusUpdates.STATUS, status);
						builder.withValue(ContactsContract.StatusUpdates.STATUS_RES_PACKAGE, "org.c99.SyncProviderDemo");
						builder.withValue(ContactsContract.StatusUpdates.STATUS_LABEL, R.string.app_name);
						builder.withValue(ContactsContract.StatusUpdates.STATUS_ICON, R.drawable.logo);
						builder.withValue(ContactsContract.StatusUpdates.STATUS_TIMESTAMP, System.currentTimeMillis());
						operationList.add(builder.build());

						//Only change the text of our custom entry to the status message pre-Honeycomb, as the newer contacts app shows
						//statuses elsewhere
						if(Integer.decode(Build.VERSION.SDK) < 11) {
							builder = ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI);
							builder.withSelection(BaseColumns._ID + " = '" + c.getLong(1) + "'", null);
							builder.withValue(ContactsContract.Data.DATA3, status);
							operationList.add(builder.build());
						}
					}
				}
			}
		} finally {
			c.close();
		}
	}
	
	private static void updateContactPhoto(ArrayList<ContentProviderOperation> operationList, long rawContactId, byte[] photo) {
		ContentProviderOperation.Builder builder = ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI);
		builder.withSelection(ContactsContract.Data.RAW_CONTACT_ID + " = '" + rawContactId 
				+ "' AND " + ContactsContract.Data.MIMETYPE + " = '" + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'", null);
		operationList.add(builder.build());

		try {
			if(photo != null) {
				builder = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
				builder.withValue(ContactsContract.CommonDataKinds.Photo.RAW_CONTACT_ID, rawContactId);
				builder.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE);
				builder.withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, photo);
				operationList.add(builder.build());

				builder = ContentProviderOperation.newUpdate(RawContacts.CONTENT_URI);
				builder.withSelection(RawContacts.CONTACT_ID + " = '" + rawContactId + "'", null);
				builder.withValue(PhotoTimestampColumn, String.valueOf(System.currentTimeMillis()));
				operationList.add(builder.build());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static class SyncEntry {
		public Long raw_id = 0L;
		public Long photo_timestamp = null;
	}

	private static void performSync(Context context, Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult)
			throws OperationCanceledException {
        Log.i("akiiiiiiiiiiiiii","sinncronizaandoooo");
        syncNoticias(provider,context);



//		HashMap<String, SyncEntry> localContacts = new HashMap<String, SyncEntry>();
//		mContentResolver = context.getContentResolver();
//		Log.i(TAG, "performSync: " + account.toString());
//
//		// Load the local contacts
//		Uri rawContactUri = RawContacts.CONTENT_URI.buildUpon().appendQueryParameter(RawContacts.ACCOUNT_NAME, account.name).appendQueryParameter(
//				RawContacts.ACCOUNT_TYPE, account.type).build();
//		Cursor c1 = mContentResolver.query(rawContactUri, new String[] { BaseColumns._ID, UsernameColumn, PhotoTimestampColumn }, null, null, null);
//		while (c1.moveToNext()) {
//			SyncEntry entry = new SyncEntry();
//			entry.raw_id = c1.getLong(c1.getColumnIndex(BaseColumns._ID));
//			entry.photo_timestamp = c1.getLong(c1.getColumnIndex(PhotoTimestampColumn));
//			localContacts.put(c1.getString(1), entry);
//		}
//
//		ArrayList<ContentProviderOperation> operationList = new ArrayList<ContentProviderOperation>();
//		try {
//			// If we don't have any contacts, create one. Otherwise, set a
//			// status message
//			if (localContacts.get("efudd") == null) {
//				addContact(account, "Elmer Fudd", "efudd");
//			} else {
//				if (localContacts.get("efudd").photo_timestamp == null || System.currentTimeMillis() > (localContacts.get("efudd").photo_timestamp + 604800000L)) {
//					//You would probably download an image file and just pass the bytes, but this sample doesn't use network so we'll decode and re-compress the icon resource to get the bytes
//					ByteArrayOutputStream stream = new ByteArrayOutputStream();
//					Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);
//					icon.compress(CompressFormat.PNG, 0, stream);
//					updateContactPhoto(operationList, localContacts.get("efudd").raw_id, stream.toByteArray());
//				}
//				updateContactStatus(operationList, localContacts.get("efudd").raw_id, "hunting wabbits");
//			}
//			if (operationList.size() > 0)
//				mContentResolver.applyBatch(ContactsContract.AUTHORITY, operationList);
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}

    }
    private static void syncNoticias(ContentProviderClient contentProviderClient,Context context){

//        AdapterItemNoticias listViewAdapter = new AdapterItemNoticias(context);

        JSONParser jParser = new JSONParser();
        // products JSONArray
        JSONArray jnoticias = null;
        // eventos novos vindo do servidos
        ArrayList<Noticia> listaNovasNoticias;


        // JSON Node names
        final String TAG_SUCCESSO = "sucesso";
        final String TAG_NOTICIAS = "noticias";
        final String TAG_CODIGO = "codigo";
        final String TAG_ASSUNTO = "assunto";
        final String TAG_DESCRICAO = "descricao";
        final String TAG_DATA = "data";
        final String TAG_HORA = "hora";
        final String TAG_CURSORELACIONADO = "cursoRelacionado";
        final String url_all_noticias = "http://10.0.0.103/aproWS/noticias/listarultimasnoticias.php";


        ControladorNoticia controladorNoticia = new ControladorNoticia(context);
        int codigoultimoNoticia = controladorNoticia.getCodigoUltimaNoticia();
//            Log.i("pegar o ultimo evento",String.valueOf(codigoultimoNoticia));
//        controladorNoticia = null;

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        // getting JSON string from URL
        JSONObject json = jParser.makeHttpRequest(url_all_noticias + "?codigo=" + String.valueOf(codigoultimoNoticia), "GET",params);

        // Check your log cat for JSON reponse
        Log.d("All Products: ", json.toString());

        try {
            // Checking for SUCCESS TAG
            int success = json.getInt(TAG_SUCCESSO);

            if (success == 1) {
                // products found
                // Getting Array of Products
//                    Log.i("json de eventos array",json.toString());
                jnoticias = json.getJSONArray(TAG_NOTICIAS);
                listaNovasNoticias = new ArrayList<Noticia>();
                // looping through All Products
                for (int i = 0; i < jnoticias.length(); i++) {
                    JSONObject c = jnoticias.getJSONObject(i);

                    // Storing each json item in variable
                    int codigo = c.getInt(TAG_CODIGO);
                    String assunto = c.getString(TAG_ASSUNTO);
                    String descricao = c.getString(TAG_DESCRICAO);
                    String data = c.getString(TAG_DATA);
                    String hora = c.getString(TAG_HORA);
                    int curoRelacionado = c.getInt(TAG_CURSORELACIONADO);

                    Noticia noticia = new Noticia(codigo, assunto, data, hora, curoRelacionado, descricao);
                    //jogar pro banco de dados
                    // exibir notificação

                    controladorNoticia.criarNoticia(noticia);

                    generateNotification(noticia,context);

//                    listaNovasNoticias.add(noticia);
                }

//                if(listaNovasNoticias!=null) {
//                    listViewAdapter.concatenarArrayDeNoticias(listaNovasNoticias);
//                    eu acho q não precisa disso pos vai atualizar e gravar no banco
//                    quando entrar na tela noticia atualiza..
//                    listViewAdapter.concatenarArrayDeNoticias(listaNovasNoticias);
//                }
//                listaNovasNoticias.clear();

            }
        }catch (JSONException e){
            e.printStackTrace();
        }

    }
    private static void generateNotification(Noticia noticia,Context context) {
        NotificationManager mNotificationManager;
        int numMessages = 0;
        Log.i("Start", "notification");

      /* Invoking the default notification service */
        NotificationCompat.Builder  mBuilder =
                new NotificationCompat.Builder(context);

        mBuilder.setContentTitle("Nova noticia");
        mBuilder.setContentText(noticia.getAssunto());
        mBuilder.setTicker("Noticia !!!");
        mBuilder.setSmallIcon(R.drawable.logo);

      /* Increase notification number every time a new notification arrives */
        mBuilder.setNumber(++numMessages);

      /* Creates an explicit intent for an Activity in your app */
        Intent resultIntent = new Intent(context, NavigationDrawer.class);

        resultIntent.setAction("NOTICIA"); //tentando linkar
        Bundle bundle = new Bundle();
        bundle.putSerializable("noticia",noticia);
        resultIntent.putExtras(bundle);
        // fim arrumar a inteçao

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(NavigationDrawer.class);

      /* Adds the Intent that starts the Activity to the top of the stack */
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);

        mNotificationManager =
//                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                (NotificationManager) getActivity().getApplication().
//                        getSystemService(getActivity().getApplication().NOTIFICATION_SERVICE);
//                (NotificationManager) getContext().getSystemService(getContext().NOTIFICATION_SERVICE);
//                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
      /* notificationID allows you to update the notification later on. */
        mNotificationManager.notify(noticia.getCodigo(), mBuilder.build());
    }
}
