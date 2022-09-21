package Kanban.Adapters;

import Kanban.Task.Task;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

    @Override
    public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
        LocalDateTime value = Objects.nonNull(localDateTime) ? localDateTime : LocalDateTime.now();
        jsonWriter.value(value.format(Task.FORMATER));
    }

    @Override
    public LocalDateTime read(JsonReader jsonReader) throws IOException {
        return LocalDateTime.parse(jsonReader.nextString(), Task.FORMATER);
    }
}
