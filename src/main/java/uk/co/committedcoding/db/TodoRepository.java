package uk.co.committedcoding.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;
import org.mapdb.DataOutput2;
import org.mapdb.DataInput2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.committedcoding.api.Todo;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Simon Casey on 10/04/2017.
 */
@Singleton
public class TodoRepository {

    static final Logger logger = LoggerFactory.getLogger(TodoRepository.class);

    private DB db;
    private ConcurrentMap<UUID, Todo> map;

    @Inject
    public TodoRepository(Database database) {
        this.db = database.getDb();
        this.map = db.hashMap("todos")
                .keySerializer(Serializer.UUID)
                .valueSerializer(new TodoSerializer())
                .createOrOpen();
//        Todo test1 = new Todo("test 1", "a test description for 1");
//        Todo test2 = new Todo("test 2");
//        Todo test3 = new Todo("test 3");
//
//        put(test1);
//        put(test2);
//        put(test3);
    }


    public Todo put(Todo todo) {
        map.put(todo.getId(), todo);
        db.commit();
        return todo;
    }

    public List<Todo> getAll() {
        return new ArrayList<>(map.values());
    }

    public void delete(Todo todo) {
        delete(todo.getId());
        db.commit();
    }

    public void delete(UUID id) {
        map.remove(id);
        db.commit();
    }

    public Optional<Todo> get(UUID id) {
        return Optional.ofNullable(map.get(id));
    }

    static class TodoSerializer implements Serializable, Serializer<Todo> {

        @Override
        public void serialize(@NotNull DataOutput2 out, @NotNull Todo value) throws IOException {
            if (value == null) {
                logger.error("Todo serializer called with 'null'");
            } else {
                out.writeUTF(new ObjectMapper().writeValueAsString(value));
            }
        }

        @Override
        public Todo deserialize(DataInput2 in, int available) throws IOException {
            String s = in.readUTF();
            return new ObjectMapper().readValue(s, Todo.class);
        }
    }

}
