package com.amazon.example.snake.aws;


import java.util.HashMap;
import java.util.Map;


import com.amazonaws.AmazonServiceException;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;


import android.os.AsyncTask;
import android.util.Log;


public class DDBGetTask extends AsyncTask<Void, Long, DDBTaskResult>
		/*implements ProgressListener*/ {

	AmazonDynamoDBClient ddb;
	DDBTaskFinishedListener listener;
	
	public interface DDBTaskFinishedListener {
		void onDDBTaskFinished(DDBTaskResult result); // If you want to pass something back to the
								// listener add a param to this method
	}

	public DDBGetTask(DDBTaskFinishedListener listener) {

		ddb = AWSClientManager.getDDB();
		this.listener = listener;
	}

	@Override
	protected void onPreExecute() {
		
	}

	protected DDBTaskResult doInBackground(Void... params) {

		Log.i("doInBackground", "Starting DDBGettask");
		
		DDBTaskResult result = new DDBTaskResult();

		try {
			// Need to specify the key of our item, which is a Map of our primary key attribute(s)
			Map<String, AttributeValue> key = new HashMap<String, AttributeValue>();
			key.put("userid", new AttributeValue().withS("user1234"));
			key.put("recordid", new AttributeValue().withS("highScore"));
			 
			GetItemRequest getItemRequest = new GetItemRequest(AWSClientManager.DDB_TABLE_NAME,key);
			GetItemResult getItemResult = ddb.getItem(getItemRequest);
			 
			result.setAttributeNumber(Integer.parseInt(getItemResult.getItem().get("data").getN()));
			
		} catch (AmazonServiceException ex) {
			ex.printStackTrace();
			Log.e("ddb-get-doInBackground", ex.getMessage());
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
			Log.i("ddb-onPostExecute:", "DDBGetTask Successful");

		listener.onDDBTaskFinished(result);
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
