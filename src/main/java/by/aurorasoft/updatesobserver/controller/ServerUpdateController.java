package by.aurorasoft.updatesobserver.controller;

import by.aurorasoft.updatesobserver.model.ServerUpdate;
import by.aurorasoft.updatesobserver.service.ServerUpdateService;
import by.aurorasoft.updatesobserver.service.factory.ServerUpdateFactory;
import by.aurorasoft.updatesobserver.util.ResponseEntityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.Duration;
import java.time.Instant;

import static by.aurorasoft.updatesobserver.util.ResponseEntityUtil.noContent;
import static by.aurorasoft.updatesobserver.util.ResponseEntityUtil.ok;
import static java.time.temporal.ChronoUnit.DAYS;

@RestController
@RequestMapping("/serverUpdate")
@RequiredArgsConstructor
public class ServerUpdateController {
    private static final Duration UPDATE_DOWNTIME_CACHE_DURATION = Duration.of(1, DAYS);

    private final ServerUpdateFactory updateFactory;
    private final ServerUpdateService updateService;

    @PostMapping
    public ResponseEntity<?> createAndSave(@RequestParam(name = "serverName") @NotBlank final String serverName,
                                           @RequestParam(name = "downtime") @Min(1) final long downtimeInMinutes,
                                           @RequestParam(name = "extraLifetime", defaultValue = "10") @Min(1) final long extraLifetimeInMinutes) {
        final ServerUpdate update = this.updateFactory.create(serverName, downtimeInMinutes, extraLifetimeInMinutes);
        this.updateService.save(update);
        return noContent();
    }

    @GetMapping
    public ResponseEntity<Instant> findUpdateDowntime(@RequestParam(name = "serverName") @NotBlank final String serverName) {
        return this.updateService.findUpdateDowntime(serverName)
                .map(datetime -> ok(datetime, UPDATE_DOWNTIME_CACHE_DURATION))
                .orElseGet(ResponseEntityUtil::noContent);
    }
}
