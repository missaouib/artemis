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

package tech.pegasys.artemis.storage;

import com.google.common.primitives.UnsignedLong;
import java.io.Closeable;
import java.util.Optional;
import org.apache.tuweni.bytes.Bytes32;
import tech.pegasys.artemis.datastructures.blocks.BeaconBlock;
import tech.pegasys.artemis.datastructures.state.BeaconState;
import tech.pegasys.artemis.storage.events.StoreDiskUpdateEvent;

public interface Database extends Closeable {

  void storeGenesis(Store store);

  void insert(StoreDiskUpdateEvent event);

  Store createMemoryStore();

  Optional<Bytes32> getFinalizedRootAtSlot(UnsignedLong slot);

  Optional<BeaconBlock> getBlock(Bytes32 root);

  Optional<BeaconState> getState(Bytes32 root);
}
