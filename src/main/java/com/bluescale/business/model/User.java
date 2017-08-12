package com.bluescale.business.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="GUI_USER")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATE_UPLOAD_FILE")
	private Date dateUploadFile;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATE_DOWNLOAD_FILE")
	private Date dateDownloadFile;

	@Id
	@Column(name="USER_EMAIL")
	private String userEmail;

	@Column(name="USER_ENABLED")
	private Boolean userEnabled;

	@Column(name="USER_PASSWORD")
	private String userPassword;
	
	@Column(name="ADMIN_ENABLED")
	private boolean adminEnabled;

	public User() {
	}

	
	public Date getDateUploadFile() {
		return this.dateUploadFile;
	}

	public void setDateUploadFile(Date dateUploadFile) {
		this.dateUploadFile = dateUploadFile;
	}

	public Date getDateDownloadFile() {
		return this.dateDownloadFile;
	}

	public void setDateDownloadFile(Date dateDownloadFile) {
		this.dateDownloadFile = dateDownloadFile;
	}


	public String getUserEmail() {
		return this.userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public Boolean getUserEnabled() {
		return this.userEnabled;
	}

	public void setUserEnabled(Boolean userEnabled) {
		this.userEnabled = userEnabled;
	}

	

	public String getUserPassword() {
	    return this.userPassword;
	}
	
	public void setUserPassword(String userPassword) {
	    this.userPassword = userPassword;
	}



	public boolean getAdminEnabled() {
		return adminEnabled;
	}



	public void setAdminEnabled(boolean adminEnabled) {
		this.adminEnabled = adminEnabled;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateUploadFile == null) ? 0 : dateUploadFile.hashCode());
		result = prime * result + ((dateDownloadFile == null) ? 0 : dateDownloadFile.hashCode());
		result = prime * result + ((userEmail == null) ? 0 : userEmail.hashCode());
		result = prime * result + ((userEnabled == null) ? 0 : userEnabled.hashCode());
		result = prime * result + ((userPassword == null) ? 0 : userPassword.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (dateUploadFile == null) {
			if (other.dateUploadFile != null)
				return false;
		} else if (!dateUploadFile.equals(other.dateUploadFile))
			return false;
		if (dateDownloadFile == null) {
			if (other.dateDownloadFile != null)
				return false;
		} else if (!dateDownloadFile.equals(other.dateDownloadFile))
			return false;
		if (userEmail == null) {
			if (other.userEmail != null)
				return false;
		} else if (!userEmail.equals(other.userEmail))
			return false;
		if (userEnabled == null) {
			if (other.userEnabled != null)
				return false;
		} else if (!userEnabled.equals(other.userEnabled))
			return false;
		if (userPassword == null) {
			if (other.userPassword != null)
				return false;
		} else if (!userPassword.equals(other.userPassword))
			return false;
		return true;
	}



	@Override
	public String toString() {
		return "User [ dateUploadFile=" + dateUploadFile + ", dateDownloadFile="
				+ dateDownloadFile + ", userEmail=" + userEmail
				+ ", userEnabled=" + userEnabled + ", userPassword=" + userPassword + ", adminEnabled=" + adminEnabled
				+ "]";
	}




	
}