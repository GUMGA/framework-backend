package gumga.framework.presentation.menu;

public class MenuItem extends MenuComponent {
	
	private String destino;
	
	public MenuItem(String label, String action) {
		super(label);
		this.destino = action;
	}

	public String getDestino() {
		return destino;
	}

	@Override
	public boolean isGrupo() {
		return false;
	}
		
}
