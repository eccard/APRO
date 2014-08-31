package recode.appro.telas;


public class DrawerItem {

	String drawerItemNome;
	String drawerHeaderTitulo;
	
	public DrawerItem(String drawerHeaderTitulo, String drawerItemNome){
		
		this.drawerHeaderTitulo = drawerHeaderTitulo;
		this.drawerItemNome = drawerItemNome;
	}

	public String getDrawerItemNome() {
		
		return drawerItemNome;
	}

	public void setDrawerItemNome(String drawerItemNome) {
		this.drawerItemNome = drawerItemNome;
	}

	public String getDrawerHeaderTitulo() {
		return drawerHeaderTitulo;
	}

	public void setDrawerHeaderTitulo(String drawerHeaderTitulo) {
		this.drawerHeaderTitulo = drawerHeaderTitulo;
	}	
	
}
