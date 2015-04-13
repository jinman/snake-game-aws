package com.amazon.example.snake.aws;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.amazonaws.regions.Regions;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.mobileconnectors.kinesis.kinesisrecorder.KinesisRecorder;
import com.amazonaws.mobileconnectors.amazonmobileanalytics.MobileAnalyticsManager;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.s3.transfermanager.TransferManager;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.lambdainvoker.*;

public class AWSClientManager {
	
	private static AmazonDynamoDBClient ddb = null;
	private static TransferManager manager = null;
	private static CognitoSyncManager cognitosync = null;
    private static CognitoCachingCredentialsProvider provider = null; 
    private static MobileAnalyticsManager analytics = null;
    private static DynamoDBMapper ddbmapper = null;
    private static KinesisRecorder kinesis = null;
    private static LambdaInvokerFactory lambda = null;
    
	public static final String AWS_ACCOUNT_ID = "<your account number from AWS Management Console/MyAccount>";
	public static final String COGNITO_POOL_ID = "<your cognito identity pool id from AWS Management Console/Cognito>";
	public static final String COGNITO_ROLE_AUTH = "<your IAM role for auth from AWS Management Console/Cognito>";
	public static final String COGNTIO_ROLE_UNAUTH = "<your IAM role for unauth from AWS Management Console/Cognito>";
	public static final String S3_BUCKET_NAME = "<your bucketname from AWS Management Console/S3>";
	public static final String DDB_TABLE_NAME = "<your table name from AWS Management Console/DynamoDB";
	public static final String KINESIS_STREAM_NAME = "<your stream name from AWS Management Console/S3";
	public static final String KINESIS_DIRECTORY_NAME = "snakegamekinesisdirectory";
	public static final String COGNITO_SYNC_DATASET_NAME = "snakegamestate";
	public static final String MOBILE_ANALYTICS_APP_ID = "SnakeGameAWS";

    
////Example
//	public static final String AWS_ACCOUNT_ID = "34234234234";
//	public static final String COGNITO_POOL_ID = "us-east-1:79187c99-9434-sddfsssssae1ba0dc8";
//	public static final String COGNITO_ROLE_AUTH = "arn:aws:iam::34234234234:role/Cognito_SnakeGamePoolAuth_DefaultRole";
//	public static final String COGNTIO_ROLE_UNAUTH = "arn:aws:iam::34234234234:role/Cognito_SnakeGamePoolUnauth_DefaultRole";
//	public static final String S3_BUCKET_NAME = "snakegamebucket";
//	public static final String DDB_TABLE_NAME = "snakegametable";
//	public static final String KINESIS_STREAM_NAME = "snakegamestream";
//	public static final String KINESIS_DIRECTORY_NAME = "snakegamekinesisdirectory";
//	public static final String COGNITO_SYNC_DATASET_NAME = "snakegamestatev8";
//	public static final String MOBILE_ANALYTICS_APP_ID = "SnakeGameAWS";

	public static void init(Context context)
	{
        provider = new CognitoCachingCredentialsProvider(context, 
                AWS_ACCOUNT_ID, COGNITO_POOL_ID, COGNTIO_ROLE_UNAUTH,
                COGNITO_ROLE_AUTH, Regions.US_EAST_1);

        //initialize the clients
        cognitosync = new CognitoSyncManager(context, Regions.US_EAST_1, provider);        
		manager = new TransferManager(provider);
		ddb = new AmazonDynamoDBClient(provider);
		//ddbmapper = new DynamoDBMapper(ddb);
		analytics = MobileAnalyticsManager.getOrCreateInstance(context, MOBILE_ANALYTICS_APP_ID, Regions.US_EAST_1, provider);
		kinesis = new KinesisRecorder(context.getDir(KINESIS_DIRECTORY_NAME, 0), Regions.US_EAST_1, provider);
		lambda = new LambdaInvokerFactory(context, Regions.US_WEST_2, provider);
	}
	
	// To use Lambda, first define methods in an interface (a placeholder)
	public interface LambdaFunctionsInterface {

	    /**
	     * Invoke lambda function "cloudFunction" (name of the function to be called in this app: localFunction
	     */
	    @LambdaFunction(functionName="cloudFunction") //this is defined in Lambda in the cloud using the Management Console.
	    String localFunction(String nameInfo);
	    
	    //Add more Lambda functions here
	}
	
	public static LambdaFunctionsInterface getLambda()
	{
		LambdaFunctionsInterface invoker = lambda.build(LambdaFunctionsInterface.class);
		return invoker;
	}
	
	public static Dataset getDataset()
	{
        return cognitosync.openOrCreateDataset(COGNITO_SYNC_DATASET_NAME);
	}
	
	
	public static AmazonDynamoDBClient getDDB() {
		if (ddb == null) {
            throw new IllegalStateException("client not initialized yet");
        }
		return ddb;
	}
	
	public static KinesisRecorder getKinesis() {
		if (kinesis == null) {
            throw new IllegalStateException("client not initialized yet");
        }
		return kinesis;
	}
	
	public static TransferManager getManager() {
		if (manager == null) {
            throw new IllegalStateException("client not initialized yet");
        }
		return manager;
	}
	
	
	 /**
     * Sets the login so that you can use authorized identity.
     * 
     * @param providerName the name of 3rd identity provider
     * @param token openId token
     */
    public static void addLogins(String providerName, String token) {
        if (provider == null) {
            throw new IllegalStateException("client not initialized yet");
        }

        Map<String, String> logins = new HashMap<String, String>();
        logins.put(providerName, token);
        provider.setLogins(logins);
        //provider.refresh();
       
    }

    /**
     * Gets the singleton instance of the CognitoClient. init() must be call
     * prior to this.
     * 
     * @return an instance of CognitoClient
     */
    public static CognitoSyncManager getCognitoSync() {
        if (cognitosync == null) {
            throw new IllegalStateException("client not initialized yet");
        }
        return cognitosync;
    }
    
    public static CognitoCachingCredentialsProvider getCognito() {
        if (provider == null) {
            throw new IllegalStateException("client not initialized yet");
        }
        return provider;
    }
    
	 public static MobileAnalyticsManager getAnalytics() {
	        if (analytics == null) {
	            throw new IllegalStateException("client not initialized yet");
	        }
	        return analytics;
	 }

}
