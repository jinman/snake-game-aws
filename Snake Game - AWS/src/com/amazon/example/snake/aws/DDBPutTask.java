package com.amazon.example.snake.aws;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;

import android.os.AsyncTask;
import android.util.Log;


public class DDBPutTask extends AsyncTask<Integer, Long, DDBTaskResult>
		/*implements ProgressListener*/ {

	AmazonDynamoDBClient ddb;
	SimpleDateFormat dateFormatter;

	public DDBPutTask() {

		ddb = AWSClientManager.getDDB();
	    dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	    dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));	
	}

	@Override
	protected void onPreExecute() {
		
	}

	protected DDBTaskResult doInBackground(Integer... params) {

		Log.i("doInBackground", "Starting DDBPuttask with user: "
				+ params[0].toString());
		DDBTaskResult result = new DDBTaskResult();

		try {
				HashMap<String, AttributeValue> item = new HashMap<String, AttributeValue>();

				AttributeValue userid = new AttributeValue().withS("user1234");
				item.put("userid", userid);

				AttributeValue recordid = new AttributeValue().withS("highScore");
				item.put("recordid", recordid);

				AttributeValue data = new AttributeValue().withN(params[0].toString());
				item.put("data", data);
				
				AttributeValue lastwritten = new AttributeValue().withS(dateFormatter.format(new Date()));
				item.put("lastwritten", lastwritten);

				PutItemRequest request = new PutItemRequest().withTableName(
						AWSClientManager.DDB_TABLE_NAME)
						.withItem(item);

				ddb.putItem(request);
			
		} catch (AmazonServiceException ex) {
			ex.printStackTrace();
			Log.e("doInBackground", ex.getMessage());
		}
	
		return result;
	}

	


	@Override
	protected void onProgressUpdate(Long... values) {
		super.onProgressUpdate(values);

	
		// it is ok to update our progress
		// bar ( a UI view ) here
		Log.i("onProgressUpdate:", values[0].toString());
	}

	// From AsyncTask, runs on UI thread when background is finished
	protected void onPostExecute(DDBTaskResult result) {
		super.onPostExecute(result);
		

		
		if (result.getErrorMessage() != null) {
			Log.e("ddb-onPostExecute:", result.getErrorMessage());
		}
		else
			Log.i("ddb-onPostExecute:", "DDBPutTask Successful");

	}

//	// From ProgressListener, publish progress to AsyncTask
//	// as this is still running in background
//	@Override
//	public void progressChanged(ProgressEvent pe) {
//		total += pe.getBytesTransferred();
//		publishProgress(total);
//		Log.i("bytestranferred:", total + "bytes");
//
//	}

}
