package Data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;

import com.minhhieu.mufi.LearnActivity;
import com.minhhieu.mufi.R;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class ExerciseAdapter extends BaseAdapter {

    private LearnActivity context;
    private int layout;
    private List<Exercise> ImageList;

    public ExerciseAdapter(LearnActivity context, int layout, List<Exercise> imageList) {
        this.context = context;
        this.layout = layout;
        ImageList = imageList;
    }

    @Override
    public int getCount() {
        return ImageList.size();
    }

    @Override
    public Object getItem(int position) {
        return ImageList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }
    private class ViewHolder{
        TextView txtTitle;
        ImageView imageTitle;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null) {
            holder = new ExerciseAdapter.ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.tvtitle);
            holder.imageTitle = (ImageView) convertView.findViewById(R.id.imagetitle);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final Exercise exercise = ImageList.get(position);
        holder.txtTitle.setText(exercise.getTitle());


        //holder.imageTitle.set;
       // chuyển byte[] -> bitmap
        byte[] ImageTitle = exercise.getImageTitle();
        Bitmap bitmap = BitmapFactory.decodeByteArray(ImageTitle,0,ImageTitle.length);

        holder.imageTitle.setImageBitmap(bitmap);





        return convertView;
    }

}
