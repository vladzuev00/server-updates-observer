package by.aurorasoft.updatesobserver.storage.factory;

import by.aurorasoft.updatesobserver.model.ServerUpdate;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

@Component
public final class ServerUpdateLoader {
    private static final TypeReference<Collection<ServerUpdate>> SERVER_UPDATES_TYPE_REFERENCE = new TypeReference<>() {
    };

    private final ObjectMapper objectMapper;
    private final File updateFile;

    public ServerUpdateLoader(final ObjectMapper objectMapper,
                              @Value("${server-updates.file-path}") final String updateFilePath) {
        this.objectMapper = objectMapper;
        this.updateFile = new File(updateFilePath);
    }

    public Collection<ServerUpdate> load() {
        try {
            return this.objectMapper.readValue(this.updateFile, SERVER_UPDATES_TYPE_REFERENCE);
        } catch (final IOException cause) {
            throw new ServerUpdateLoadingException(cause);
        }
    }

    static final class ServerUpdateLoadingException extends RuntimeException {

        @SuppressWarnings("unused")
        public ServerUpdateLoadingException() {

        }

        @SuppressWarnings("unused")
        public ServerUpdateLoadingException(final String description) {
            super(description);
        }

        public ServerUpdateLoadingException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public ServerUpdateLoadingException(final String description, final Exception cause) {
            super(description, cause);
        }

    }
}
