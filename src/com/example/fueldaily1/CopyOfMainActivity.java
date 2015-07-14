package com.example.fueldaily1;

import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


public class CopyOfMainActivity extends Activity {

   VideoView videoview;
   private MediaController mediaControls;
   private ProgressDialog progressDialog;
   String bucketName = "fueldaily";
   AmazonS3 s3;
   ObjectListing objectListing;
   Uri uri;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
         StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
        .permitAll().build();
        
         StrictMode.setThreadPolicy(policy);

         if(!isNetworkAvailable()) {
        	   Toast.makeText(this,"No Internet connection",Toast.LENGTH_LONG).show();
        	   finish(); //Calling this method to close this activity when internet is not available.
        } else {
         
         if (mediaControls == null) {
            	  mediaControls = new MediaController(CopyOfMainActivity.this);
    	 }


		ArrayList<String> list_videos_from_bucket = new ArrayList<String>();

		list_videos_from_bucket = getObjectList();
	
	    ArrayAdapter<String> listvideosArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,  list_videos_from_bucket);

	    ListView list_videos_from_bucket_aslist = (ListView)findViewById(R.id.listView1);
	 
	    list_videos_from_bucket_aslist.setAdapter(listvideosArrayAdapter);
	 
	    list_videos_from_bucket_aslist.setOnItemClickListener(new OnItemClickListener() {
         
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long id) {
		     String item = ((TextView)view).getText().toString();
             
             Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();
             
             setContentView(R.layout.video_main);
     		
     		// videoview = (VideoView) findViewById(R.id.video_player_view);

     		 try {
     			videoview.setMediaController(mediaControls);

     		 } catch (Exception e) {
     			 Log.e("Error in Media Controls", e.getMessage());
     		     e.printStackTrace();

     		 }
     		 
     		  GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, item);
     	      URL objectURL = s3.generatePresignedUrl(request);
     		  uri=Uri.parse(objectURL.toString());      
       	 	  
              videoview.setVideoURI(uri);
     	      videoview.requestFocus();
     	 
     		 videoview.start();
	 	  }
	 });
        }
	 }
	


	public ArrayList<String> getObjectList(){
		
		s3 = new AmazonS3Client(new BasicAWSCredentials("AKIAIL5P46R7BWHUK4JA", "YvBz0m95efCfDGRe/uOEwWz/bd09RRZ5ROYF4QY3"));  
			    
	   objectListing = s3.listObjects(new ListObjectsRequest().withBucketName(bucketName));
	    	  
	    ArrayList<String> nodeList = new ArrayList<String>();	
	    
	    for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {

		    nodeList.add(objectSummary.getKey());
		   
	    }  
		return nodeList;
	}
	
	private boolean isNetworkAvailable() {
	    boolean haveConnectedWifi = false;
	    boolean haveConnectedMobile = false;

	    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo[] netInfo = cm.getAllNetworkInfo();
	    for (NetworkInfo ni : netInfo) {
	        if (ni.getTypeName().equalsIgnoreCase("WIFI"))
	            if (ni.isConnected())
	                haveConnectedWifi = true;
	        if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
	            if (ni.isConnected())
	                haveConnectedMobile = true;
	    }
	    return haveConnectedWifi || haveConnectedMobile;
	}
}
