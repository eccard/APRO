package recode.appro.telas;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;

public class ActivityPesquisa extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_pesquisa);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		 
		MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.menu.pesquisa, menu);
		    return true;
	}

}
