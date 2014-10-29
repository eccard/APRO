package recode.appro.main.view;

import android.app.ActionBar;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.facebook.widget.ProfilePictureView;

import org.c99.SyncProviderDemo.LoginActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import recode.appro.evento.view.FragmentEvento;
import recode.appro.evento.view.FragmentEventoConfrimados;
import recode.appro.evento.view.FragmentEventos;
import recode.appro.faculdade.extras.view.FragmentCalendario;
import recode.appro.faculdade.extras.view.FragmentUniversidadeInfo;
import recode.appro.noticia.controle.ControladorNoticia;
import recode.appro.main.controle.ControladorUsuario;
import recode.appro.faculdade.curso.view.FragmentCursoMain;
import recode.appro.faculdade.departamento.view.FragmentDepartamentoMain;
import recode.appro.faculdade.departamento.view.FragmentProfessores;
import recode.appro.evento.Evento;
import recode.appro.main.FragmentListener;
import recode.appro.noticia.modelo.Noticia;
import recode.appro.noticia.view.FragmentNoticia;
import recode.appro.noticia.view.FragmentNoticias;
import recode.appro.telas.R;

public class NavigationDrawer extends FragmentActivity implements
        OnGroupClickListener, OnChildClickListener,FragmentListener {

    private DrawerLayout mDrawerLayout;
    private ExpandableListView mDrawerList;
    private static ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mSubTitle;
    private CharSequence mTitle = "Notícias";
    private AdapterDrawerItem adapterCustomDrawer;
    public static int codigoCurso;
    public static int codigoDepartamento;


    private String TAG = "NavigationDrawer";
    private FaceFragment faceFragment; //facebook
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Log.i(TAG,"----------");
        Log.i(TAG,getApplication().getFilesDir().toString());
//        Fragment fragment = null;
//        android.support.v4.app.FragmentManager frgManager;

        //implementando o faceboock

//        if (savedInstanceState == null) {
//            Log.i(TAG," saved instance == null");
//            // Add the fragment on initial activity setup
//            faceLogin = new FaceLogin();
//            android.support.v4.app.FragmentManager frgManager = getSupportFragmentManager();
//            frgManager.beginTransaction().replace(R.id.content_frame, faceLogin)
//                    .commit();
//
//        } else {
//            Log.i(TAG," saved instance != null");
//            // Or set the fragment from restored state info
//            faceLogin = (FaceLogin) getSupportFragmentManager()
//                    .findFragmentById(R.id.content_frame);
//
//        }

        //implementando o facebooc

        // verificar se já tem usuario caso não tenho um registrar
        ControladorUsuario controladorUsuario  = new ControladorUsuario(getApplicationContext());

        if(controladorUsuario.verificarSeExisteUsuario()==0){//se não existe usuario,criar
           Intent intent = new Intent(this, FaceLogin.class);
            startActivity(intent);
            Log.i(TAG,"usuario não criado");
//            faceLogin = new FaceLogin();

//            faceFragment = new FaceFragment();
//            android.support.v4.app.FragmentManager frgManager;
//            frgManager = getSupportFragmentManager();
//            frgManager.beginTransaction()
////                    .replace(R.id.content_frame, FaceLogin.newInstance(null,null)).commit();
//                    .replace(R.id.content_frame, faceFragment).commit();


            //fim verificar se já tem usuario caso não tenho um registrar
        }
        else {



            Fragment fragment = null;

//
            // inicio para capturar a acao da intencao para as notificacoes
            Intent intent = getIntent();
            try {
                String action = intent.getAction().toUpperCase();
                if (action != null) {
                    Log.i("entrou na acçao", "ennnntroouuu  " + action);

                    if (action.equalsIgnoreCase("NOTICIA")) {
                        ControladorNoticia controladorNoticia = new ControladorNoticia(getApplicationContext());

                        Noticia noticia = (Noticia) intent.getSerializableExtra("noticia");
                        controladorNoticia.setVisualizarNoticia1(noticia.getCodigo());

                        Log.i("noticia infos", noticia.getAssunto() + noticia.getDescricao());
                        Log.i("que porra e eassa", "paaapaaaa");
                        NotificationManager mNotificationManager = (NotificationManager) getApplication()
                                .getSystemService(getApplication().NOTIFICATION_SERVICE);

                        mNotificationManager.cancel(noticia.getCodigo());

                        fragment = new FragmentNoticia(noticia);
                        android.support.v4.app.FragmentManager frgManager = getSupportFragmentManager();
                        frgManager.beginTransaction().replace(R.id.content_frame, fragment)
                                .commit();
                        mDrawerLayout.closeDrawer(mDrawerList);
                        Log.i(" 111111111111", "1111111111");
                    }
                    if (action.equalsIgnoreCase("EVENTO")) {
//                    ControladorEvento controladorEvento = new ControladorEvento(getApplicationContext());

                        Evento evento = (Evento) intent.getSerializableExtra("evento");
                        Log.i("evento infos", evento.getNome() + evento.getDescricao());

                        NotificationManager mNotificationManager = (NotificationManager) getApplication()
                                .getSystemService(getApplication().NOTIFICATION_SERVICE);

                        mNotificationManager.cancel(evento.getCodigo());

                        //fragment = new FragmentEvento(evento);
                        callbackEvento(evento);

//                    android.support.v4.app.FragmentManager frgManager = getSupportFragmentManager();
//                    frgManager.beginTransaction().replace(R.id.content_frame, fragment)
//                            .commit();
                        mDrawerLayout.closeDrawer(mDrawerList);

                    }
                    if (action.equalsIgnoreCase("ANDROID.INTENT.ACTION.MAIN")) {
                        if (savedInstanceState == null) {
                            // e
                            Log.i("entrano em noticias", "entrando em noticias");
                            fragment = new FragmentNoticias();
                            getActionBar().removeAllTabs();
                            getActionBar()
                                    .setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);


                            android.support.v4.app.FragmentManager frgManager = getSupportFragmentManager();
                            frgManager.beginTransaction().replace(R.id.content_frame, fragment)
                                    .commit();
                            mDrawerLayout.closeDrawer(mDrawerList);
                        }
                    }
                } else {
                    Log.i("action == null", "action == null");

                }
            } catch (Exception e) {
                Log.e("NavigationDrawer ", "Problem consuming action from intent", e);
            }


            // final para capturar a acao da intencao para as notificacoes

        }

        // Inicializando itens do Navigation Drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ExpandableListView) findViewById(R.id.left_drawer);

        adapterCustomDrawer = new AdapterDrawerItem(this);

        mDrawerList.setAdapter(adapterCustomDrawer);
        mDrawerList.setOnGroupClickListener(this);
        mDrawerList.setOnChildClickListener(this);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_navigation_drawer, R.string.drawer_open,
                R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle("PUROmobile");
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()

            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return false;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v,
                                int groupPosition, int childPosition, long id) {

        // Bundle bundle;
        Fragment fragment = null;
        android.support.v4.app.FragmentManager frgManager;

        switch (groupPosition) {
            case 2:

                getActionBar().removeAllTabs();
                getActionBar()
                        .setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

                switch (childPosition) {

                    case 0:

                        // bundle = new Bundle();
                        // bundle.putInt("codigoCurso", 1);
                        //
                        mTitle = "Ciência da Computação";
                        getActionBar().setTitle(mTitle);
                        getActionBar().setSubtitle("Cursos");

                        NavigationDrawer.codigoCurso = 1;
                        fragment = new FragmentCursoMain();

                        // fragment.setArguments(bundle);

                        frgManager = getSupportFragmentManager();
                        frgManager.beginTransaction()
                                .replace(R.id.content_frame, fragment).commit();

                        mDrawerLayout.closeDrawer(mDrawerList);
                        break;

                    case 1:

                        // bundle = new Bundle();
                        // bundle.putInt("codigoCurso", 6);
                        mTitle = "Enfermagem";
                        getActionBar().setTitle(mTitle);
                        getActionBar().setSubtitle("Cursos");

                        NavigationDrawer.codigoCurso = 6;
                        fragment = new FragmentCursoMain();

                        // fragment.setArguments(bundle);

                        frgManager = getSupportFragmentManager();
                        frgManager.beginTransaction()
                                .replace(R.id.content_frame, fragment).commit();

                        mDrawerLayout.closeDrawer(mDrawerList);
                        break;

                    case 2:

                        mTitle = "Engenharia de Produção";
                        getActionBar().setTitle(mTitle);
                        getActionBar().setSubtitle("Cursos");

                        NavigationDrawer.codigoCurso = 3;
                        fragment = new FragmentCursoMain();
                        // fragment.setArguments(bundle);

                        frgManager = getSupportFragmentManager();
                        frgManager.beginTransaction()
                                .replace(R.id.content_frame, fragment).commit();

                        mDrawerLayout.closeDrawer(mDrawerList);
                        break;

                    case 3:

                        mTitle = "Produção Cultural";
                        getActionBar().setTitle(mTitle);
                        getActionBar().setSubtitle("Cursos");

                        NavigationDrawer.codigoCurso = 2;
                        fragment = new FragmentCursoMain();
                        // fragment.setArguments(bundle);

                        frgManager = getSupportFragmentManager();
                        frgManager.beginTransaction()
                                .replace(R.id.content_frame, fragment).commit();

                        mDrawerLayout.closeDrawer(mDrawerList);
                        break;

                    case 4:

                        mTitle = "Psicologia";
                        getActionBar().setTitle(mTitle);
                        getActionBar().setSubtitle("Cursos");

                        NavigationDrawer.codigoCurso = 5;
                        fragment = new FragmentCursoMain();
                        // fragment.setArguments(bundle);

                        frgManager = getSupportFragmentManager();
                        frgManager.beginTransaction()
                                .replace(R.id.content_frame, fragment).commit();

                        mDrawerLayout.closeDrawer(mDrawerList);
                        break;

                    case 5:

                        mTitle = "Serviço Social";
                        getActionBar().setTitle(mTitle);
                        getActionBar().setSubtitle("Cursos");

                        NavigationDrawer.codigoCurso = 4;
                        fragment = new FragmentCursoMain();
                        // fragment.setArguments(bundle);

                        frgManager = getSupportFragmentManager();
                        frgManager.beginTransaction()
                                .replace(R.id.content_frame, fragment).commit();

                        mDrawerLayout.closeDrawer(mDrawerList);
                        break;

                    default:
                        break;
                }

                break;

            case 3:

                getActionBar().removeAllTabs();
                getActionBar()
                        .setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

                switch (childPosition) {

                    case 0:

                        mTitle = "ICT";
                        getActionBar().setTitle(mTitle);
                        getActionBar().setSubtitle("Departamentos");

                        NavigationDrawer.codigoDepartamento = 4;
                        fragment = new FragmentDepartamentoMain();

                        frgManager = getSupportFragmentManager();
                        frgManager.beginTransaction()
                                .replace(R.id.content_frame, fragment).commit();

                        mDrawerLayout.closeDrawer(mDrawerList);
                        break;

                    case 1:

                        mTitle = "IHS";
                        getActionBar().setTitle(mTitle);
                        getActionBar().setSubtitle("Departamentos");

                        NavigationDrawer.codigoDepartamento = 3;
                        fragment = new FragmentDepartamentoMain();

                        frgManager = getSupportFragmentManager();
                        frgManager.beginTransaction()
                                .replace(R.id.content_frame, fragment).commit();

                        mDrawerLayout.closeDrawer(mDrawerList);
                        break;

                    case 2:

                        mTitle = "RAE";
                        getActionBar().setTitle(mTitle);
                        getActionBar().setSubtitle("Departamentos");

                        NavigationDrawer.codigoDepartamento = 5;
                        fragment = new FragmentDepartamentoMain();

                        frgManager = getSupportFragmentManager();
                        frgManager.beginTransaction()
                                .replace(R.id.content_frame, fragment).commit();

                        mDrawerLayout.closeDrawer(mDrawerList);
                        break;

                    case 3:

                        mTitle = "RCT";
                        getActionBar().setTitle(mTitle);
                        getActionBar().setSubtitle("Departamentos");

                        NavigationDrawer.codigoDepartamento = 2;
                        fragment = new FragmentDepartamentoMain();

                        frgManager = getSupportFragmentManager();
                        frgManager.beginTransaction()
                                .replace(R.id.content_frame, fragment).commit();

                        mDrawerLayout.closeDrawer(mDrawerList);
                        break;

                    case 4:

                        mTitle = "RFM";
                        getActionBar().setTitle(mTitle);
                        getActionBar().setSubtitle("Departamentos");

                        NavigationDrawer.codigoDepartamento = 1;
                        fragment = new FragmentDepartamentoMain();

                        frgManager = getSupportFragmentManager();
                        frgManager.beginTransaction()
                                .replace(R.id.content_frame, fragment).commit();

                        mDrawerLayout.closeDrawer(mDrawerList);
                        break;

                    case 5:

                        mTitle = "RIR";
                        getActionBar().setTitle(mTitle);
                        getActionBar().setSubtitle("Departamentos");

                        NavigationDrawer.codigoDepartamento = 6;
                        fragment = new FragmentDepartamentoMain();

                        frgManager = getSupportFragmentManager();
                        frgManager.beginTransaction()
                                .replace(R.id.content_frame, fragment).commit();

                        mDrawerLayout.closeDrawer(mDrawerList);
                        break;

                }

            default:
                break;
        }

        return false;
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v,
                                int groupPosition, long id) {

        Fragment fragment = null;
        android.support.v4.app.FragmentManager frgManager;

        switch (groupPosition) {

            case 0:

                mTitle = "O Pólo";
                getActionBar().setTitle(mTitle);

                getActionBar().removeAllTabs();
                getActionBar()
                        .setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

                fragment = new FragmentUniversidadeInfo();
                frgManager = getSupportFragmentManager();
                frgManager.beginTransaction().replace(R.id.content_frame, fragment)
                        .commit();
                mDrawerLayout.closeDrawer(mDrawerList);
                break;

            case 1:

                mTitle = "Calendários";
                getActionBar().setTitle(mTitle);

                getActionBar().removeAllTabs();
                getActionBar()
                        .setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

                fragment = new FragmentCalendario();
                frgManager = getSupportFragmentManager();
                frgManager.beginTransaction().replace(R.id.content_frame, fragment)
                        .commit();
                mDrawerLayout.closeDrawer(mDrawerList);
                break;

            case 4:

                mTitle = "Notícias";
                getActionBar().setTitle(mTitle);

                fragment = new FragmentNoticias();
                getActionBar().removeAllTabs();
                getActionBar()
                        .setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

                frgManager = getSupportFragmentManager();
                frgManager.beginTransaction().replace(R.id.content_frame, fragment)
                        .commit();
                mDrawerLayout.closeDrawer(mDrawerList);

                break;

            case 5:

                mTitle = "Professores";
                getActionBar().setTitle(mTitle);

                fragment = new FragmentProfessores();
                getActionBar().removeAllTabs();
                getActionBar()
                        .setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

                frgManager = getSupportFragmentManager();
                frgManager.beginTransaction().replace(R.id.content_frame, fragment)
                        .commit();
                mDrawerLayout.closeDrawer(mDrawerList);

                break;
            case 6:

                mTitle = "Eventos";
                getActionBar().setTitle(mTitle);

                fragment = new FragmentEventos();
                getActionBar().removeAllTabs();
                getActionBar()
                        .setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

                frgManager = getSupportFragmentManager();
                frgManager.beginTransaction().replace(R.id.content_frame, fragment)
                        .commit();
                mDrawerLayout.closeDrawer(mDrawerList);

                break;


            default:
                break;
        }

        return false;
    }

    @Override
    public void callbackEvento(Evento evento) {
        getActionBar().setTitle("Evento");
       getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, FragmentEvento.newInstance(evento)).commit();
    }

    @Override
    public void callbackEventoConfirmados(Evento evento) {
        getActionBar().setTitle("Usuarios Confirmados");
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, FragmentEventoConfrimados.newInstance(evento.getCodigo())).commit();
    }
}
