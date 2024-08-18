package http;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class DurationAdapter extends TypeAdapter<Duration> {
    @Override
    public void write(JsonWriter out, Duration value) throws IOException {
        String time = value.toString();
        out.value(time);
    }

    @Override
    public Duration read(JsonReader in) throws IOException {
        String time = in.nextString();
        return Duration.parse(time);
    }
}
