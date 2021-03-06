/*
 * Copyright 2019 ConsenSys AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package tech.pegasys.artemis.networking.eth2;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;
import tech.pegasys.artemis.datastructures.blocks.BeaconBlock;
import tech.pegasys.artemis.networking.eth2.gossip.events.GossipedBlockEvent;

public class GossipedBlockCollector {
  private final Collection<BeaconBlock> blocks = new ConcurrentLinkedQueue<>();

  public GossipedBlockCollector(final EventBus eventBus) {
    eventBus.register(this);
  }

  @Subscribe
  public void onBeaconBlock(final GossipedBlockEvent event) {
    blocks.add(event.getBlock());
  }

  public Collection<BeaconBlock> getBlocks() {
    return blocks;
  }
}
