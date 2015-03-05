package com.amazon.example.snake.aws;


public class KinesisTaskResult {
	String attributeString = null;
	int attributeNumber;
	String errorMessage = null;

	// Getters and setters


	public String getErrorMessage() {
		return errorMessage;
	}

	public String getAttributeString() {
		return attributeString;
	}

	public void setAttributeString(String attributeString) {
		this.attributeString = attributeString;
	}

	public int getAttributeNumber() {
		return attributeNumber;
	}

	public void setAttributeNumber(Integer attributeNumber) {
		this.attributeNumber = attributeNumber;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}