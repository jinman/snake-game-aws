package com.amazon.example.snake.aws;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.amazon.example.snake.aws.AWSClientManager.LambdaFunctionsInterface;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;

public class LambdaSyncTask extends AsyncTask<String, Void, String> {
	
	  LambdaFunctionsInterface lambda;
	  Activity callingActivity;

	public LambdaSyncTask(Activity callingActivity) {

        lambda = AWSClientManager.getLambda();
        this.callingActivity = callingActivity;
    }
	
    @Override
    protected String doInBackground(String... params) {
        // invoke "echo" method. In case it fails, it will throw a
        // LambdaFunctionException.
        try {
            return lambda.localFunction(params[0]);
        } catch (LambdaFunctionException lfe) {
            Log.e("lambda-doInBackground", "Failed to invoke localFunction", lfe);
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (result == null) {
            return;
        }

        Toast.makeText(callingActivity, result, Toast.LENGTH_LONG).show();
    }
}
