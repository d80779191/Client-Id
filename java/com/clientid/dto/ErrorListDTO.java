package com.clientid.dto;

import java.io.Serializable;
import java.util.ArrayList;

public class ErrorListDTO extends ArrayList<ErrorDTO>  implements Serializable{
	private static final long serialVersionUID = 1L;
	public ErrorListDTO() {
		super();
	}
	public ErrorListDTO(ErrorDTO item) {
		this.add(item);
	}
	
}
