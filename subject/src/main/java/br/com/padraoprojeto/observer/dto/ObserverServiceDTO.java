package br.com.padraoprojeto.observer.dto;

public class ObserverServiceDTO {
	private String uri;

	public ObserverServiceDTO() {
		super();
	}
	
	public ObserverServiceDTO(String uri) {
		super();
		this.uri = uri;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	
}
