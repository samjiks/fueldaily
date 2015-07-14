package com.example.fueldaily1;
 
import java.util.ArrayList;
import java.util.HashMap;
 
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
public class LazyAdapter extends BaseAdapter {
 
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;

 
    public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
 
    public int getCount() {
        return data.size();
    }
 
    public Object getItem(int position) {
        return position;
    }
 
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.listlayout, null);
 
        TextView firstline = (TextView)vi.findViewById(R.id.firstLine); // title
        TextView secondline = (TextView)vi.findViewById(R.id.secondLine); // Description
      //  ImageView thumb_image=(ImageView)vi.findViewById(R.id.icon); // thumb image
 
        HashMap<String, String> video = new HashMap<String, String>();
        video = data.get(position);
 
        // Setting all values in listview
     //   firstline.setText(video.get(MainActivity.FIRST_LINE));
    //    secondline.setText(video.get(MainActivity.SECOND_LINE));

        return vi;
    }
}