package com.sourcery.sport.mocks;

import com.sourcery.sport.tournamenttag.model.TournamentTag;
import com.sourcery.sport.tournamenttag.service.TournamentTagService;
import java.util.*;

public class TournamentTagServiceMock implements TournamentTagService {
  private final Map<UUID, TournamentTag> tagStore = new HashMap<>();

  @Override
  public TournamentTag saveTag(TournamentTag tag) {
    tagStore.put(tag.getId(), tag);
    return tag;
  }

  @Override
  public TournamentTag getTagById(UUID id) {
    return tagStore.get(id);
  }

  @Override
  public List<TournamentTag> getAllTags() {
    return new ArrayList<>(tagStore.values());
  }
}

