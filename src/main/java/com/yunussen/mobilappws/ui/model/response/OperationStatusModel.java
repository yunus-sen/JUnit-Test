package com.yunussen.mobilappws.ui.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class OperationStatusModel {

	private String actionResult;
	private String actionName;
}
