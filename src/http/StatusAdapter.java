package http;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import model.Status;

import java.io.IOException;

public class StatusAdapter extends TypeAdapter<Status> {
    @Override
    public void write(JsonWriter out, Status value) throws IOException {
        String str = value.toString();
        out.value(str);
    }

    @Override
    public Status read(JsonReader in) throws IOException {
        String str = in.nextString();
        return Status.valueOf(str);
    }
}
