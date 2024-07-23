package br.com.padraoprojeto.observer.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ServiceOrder {
	private int id;

	private String status;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime creationDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime lastUpdate;
	
	private boolean processed;

	public ServiceOrder() {
		
	}
	
	public ServiceOrder(String status) {
		super();
		this.status = status;
		this.creationDate = LocalDateTime.now();
		this.lastUpdate = LocalDateTime.now();
		this.processed = false;
	}
	
	public ServiceOrder(int id, String status, LocalDateTime creationDate, LocalDateTime lastUpdate, boolean processed) {
		super();
		this.id = id;
		this.status = status;
		this.creationDate = creationDate;
		this.lastUpdate = lastUpdate;
		this.processed = processed;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public LocalDateTime getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(LocalDateTime lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public boolean isProcessed() {
		return processed;
	}

	public void setProcessed(boolean processed) {
		this.processed = processed;
	}

}
