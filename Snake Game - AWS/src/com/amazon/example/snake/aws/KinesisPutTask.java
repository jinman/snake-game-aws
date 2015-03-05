package com.amazon.example.snake.aws;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.mobileconnectors.kinesis.kinesisrecorder.KinesisRecorder;

import android.os.AsyncTask;
import android.util.Log;

public class KinesisPutTask extends AsyncTask<Integer, Long, KinesisTaskResult>
/* implements ProgressListener */{

	KinesisRecorder kinesis;
	SimpleDateFormat dateFormatter;

	public KinesisPutTask() {

		kinesis = AWSClientManager.getKinesis();
		dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	@Override
	protected void onPreExecute() {

	}

	protected KinesisTaskResult doInBackground(Integer... params) {

		Log.i("kinesis-doInBackground", "Starting KinesisPuttask with user: "
				+ params[0].toString());
		KinesisTaskResult result = new KinesisTaskResult();

		try {
			
			Process process = Runtime.getRuntime().exec(new String[]{"logcat", "-d","-s","*:w"});
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()),4*1024);
			String line;
			  final StringBuilder log = new StringBuilder();
			  String separator = System.getProperty("line.separator"); 
			    while ((line = br.readLine()) != null)
			     {
			       log.append(line);
			         log.append(separator);
			}
		    
		      String dataStr = "kinesissnake/gameerrors/" + log.toString();
		      Log.i("kinesis-doInBackground", dataStr);
			kinesis.saveRecord(dataStr.getBytes(), AWSClientManager.KINESIS_STREAM_NAME);
			String dataStr1 = "kinesissnake/snakescore/"+ String.valueOf(params[1]);
			kinesis.saveRecord(dataStr1.getBytes(), AWSClientManager.KINESIS_STREAM_NAME);
		} catch (AmazonServiceException ex) {
			ex.printStackTrace();
			Log.e("kinesis-doInBackground", ex.getMessage());
		}
		catch (IOException e) {
			e.printStackTrace();
			Log.e("kinesis-doInBackground", e.getMessage());
	    }
		
		kinesis.submitAllRecords();

		return result;
	}

	@Override
	protected void onProgressUpdate(Long... values) {
		super.onProgressUpdate(values);

		// it is ok to update our progress
		// bar ( a UI view ) here
		Log.i("kinesis-onProgressUpdate:", values[0].toString());
	}

	// From AsyncTask, runs on UI thread when background is finished
	protected void onPostExecute(KinesisTaskResult result) {
		super.onPostExecute(result);

	

		if (result.getErrorMessage() != null) {
			Log.e("kinesis-onPostExecute:", result.getErrorMessage());
		} else
			Log.i("kinesis-onPostExecute:", "KinesisPutTask Successful");

	}

}
