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

package tech.pegasys.artemis.networking.p2p.libp2p;

import io.libp2p.core.Connection;
import io.libp2p.core.PeerId;
import java.util.Map;
import org.apache.tuweni.bytes.Bytes;
import tech.pegasys.artemis.networking.p2p.libp2p.rpc.RpcHandler;
import tech.pegasys.artemis.networking.p2p.peer.NodeId;
import tech.pegasys.artemis.networking.p2p.peer.Peer;
import tech.pegasys.artemis.networking.p2p.rpc.RpcMethod;
import tech.pegasys.artemis.networking.p2p.rpc.RpcRequestHandler;
import tech.pegasys.artemis.networking.p2p.rpc.RpcStream;
import tech.pegasys.artemis.util.async.SafeFuture;

public class LibP2PPeer implements Peer {

  private final Map<RpcMethod, RpcHandler> rpcHandlers;
  private final Connection connection;
  private final NodeId nodeId;

  public LibP2PPeer(final Connection connection, final Map<RpcMethod, RpcHandler> rpcHandlers) {
    this.connection = connection;
    this.rpcHandlers = rpcHandlers;

    final PeerId peerId = connection.getSecureSession().getRemoteId();
    nodeId = new LibP2PNodeId(peerId);
  }

  @Override
  public NodeId getId() {
    return nodeId;
  }

  @Override
  public boolean isConnected() {
    return connection.getNettyChannel().isOpen();
  }

  @Override
  @SuppressWarnings("FutureReturnValueIgnored")
  public void disconnect() {
    connection.getNettyChannel().close();
  }

  @Override
  public SafeFuture<RpcStream> sendRequest(
      RpcMethod rpcMethod, final Bytes initialPayload, final RpcRequestHandler handler) {
    RpcHandler rpcHandler = rpcHandlers.get(rpcMethod);
    if (rpcHandler == null) {
      throw new IllegalArgumentException("Unknown rpc method invoked: " + rpcMethod.getId());
    }
    return rpcHandler.sendRequest(connection, initialPayload, handler);
  }

  @Override
  public boolean connectionInitiatedLocally() {
    return connection.isInitiator();
  }

  @Override
  public boolean connectionInitiatedRemotely() {
    return !connectionInitiatedLocally();
  }
}
