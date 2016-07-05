package xyz.gatling.ora;

import android.content.Context;
import android.os.AsyncTask;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gimmi on 7/4/2016.
 */

public class S3Task extends AsyncTask<Void, Void, Void> {

    public interface OnFinished{
        void onFinished();
    }

    AmazonS3 amazonS3;
    OnFinished onFinished;

    S3Task(Context context){
        this(context, null);
    }

    S3Task(Context context, OnFinished onFinished){
        this.onFinished = onFinished;
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                context,
                "us-east-1:f7c780f0-a8c2-41ba-b9ef-de96695ab721", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );
        amazonS3 = new AmazonS3Client(credentialsProvider);
        amazonS3.setRegion(Region.getRegion(Regions.US_EAST_1));
        OraWorkerService.organizationToGreekMapping = new HashMap<>();
    }

    @Override
    protected Void doInBackground(Void... params) {
        String bucketName = "xyz.gatling.ora.heroimages";
        ListObjectsRequest request = new ListObjectsRequest(
                bucketName,
                null,
                null,
                null,
                Integer.MAX_VALUE);
        OraWorkerService.s3listings = amazonS3.listObjects(request);

//            Log.v("TAVON", "Updated items");

        S3Object mapping = amazonS3.getObject(
                bucketName,
                OraWorkerService.s3listings.getObjectSummaries().get(OraWorkerService.s3listings.getObjectSummaries().size()-1).getKey());
        S3ObjectInputStream mappingStream = mapping.getObjectContent();
        InputStreamReader streamReader = new InputStreamReader(mappingStream);
        OraWorkerService.organizationToGreekMapping = new Gson().fromJson(streamReader, new TypeToken<Map<String, String>>(){}.getType());

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        OraWorkerService.organizationToHeroMapping = new HashMap<>();

        List<S3ObjectSummary> summaries = OraWorkerService.s3listings.getObjectSummaries();
        for(S3ObjectSummary summary : summaries){
            String[] dirHero = summary.getKey().split("/");
            if(dirHero.length == 1){
                continue;
            }
            List<String> heros = OraWorkerService.organizationToHeroMapping.get(dirHero[0]);
            if(heros == null){
                heros = new ArrayList<>();
            }
            heros.add(dirHero[1].replace(".png", ""));
            OraWorkerService.organizationToHeroMapping.put(dirHero[0], heros);
        }

        OraWorkerService.organizationToHeroMapping.remove("Ora");
        if(onFinished != null){
            onFinished.onFinished();
        }
    }
}
