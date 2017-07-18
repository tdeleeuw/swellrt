package org.swellrt.beta.model.wave;

import org.swellrt.beta.client.operation.impl.OpenOperation;
import org.swellrt.beta.common.SException;
import org.swellrt.beta.model.SHandlerFunc;
import org.swellrt.beta.model.SList;
import org.swellrt.beta.model.SMap;
import org.swellrt.beta.model.SNode;
import org.swellrt.beta.model.SObject;
import org.swellrt.beta.model.SObservableNode;
import org.swellrt.beta.model.SPrimitive;
import org.swellrt.beta.model.SStatusEvent;
import org.swellrt.beta.model.SVisitor;
import org.swellrt.beta.model.js.Proxy;
import org.swellrt.beta.model.js.SMapProxyHandler;
import org.waveprotocol.wave.model.wave.InvalidParticipantAddress;

import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOptional;


/**
 *
 * Main class providing object-based data model built on top of Wave data model.
 * <p>
 * A {@link SObject} represents a JSON-like data structure containing a nested
 * structure of {@link SMap}, {@link SList} and {@link SPrimitive} values.
 * <p>
 * The underlying implementation based on the Wave data model provides:
 * <ul>
 * <li>Real-time sync of SObject state from changes of different users, even
 * between different servers (see Wave Federation)</li>
 * <li>In-flight persistence of changes with eventual consistency (see Wave
 * Operational Transformations)</li>
 * </ul>
 * <p>
 *
 * @author pablojan@gmail.com (Pablo Ojanguren)
 *
 */
public class SWaveObject extends SWaveNodeContainer
    implements SObject, SObservableNode, OpenOperation.Response {


  private SWaveMap root;

  private SObject.StatusHandler statusHandler = null;
  private final SWaveNodeManager waveManager;

  /**
   * Get a MutableCObject instance with a substrate Wave.
   * Initialize the Wave accordingly.
   *
   */
  public static SWaveObject materialize(SWaveNodeManager waveManager) {
    SWaveObject object = new SWaveObject(waveManager);
    object.init();
    waveManager.setListener(new SWaveletListener(object));
    return object;
  }

  /**
   * Private constructor.
   *
   * @param idGenerator
   * @param domain
   * @param wave
   */
  protected SWaveObject(SWaveNodeManager waveManager) {
    this.waveManager = waveManager;
  }

  /**
   * Initialization tasks not suitable for constructors.
   */
  private void init() {
    root = waveManager.loadRoot();
    root.attach(SWaveNodeContainer.Void);
  }

  /**
   * Unit test only.
   * TODO fix visibility
   */
  @Override
  protected void clearCache() {
    root.clearCache();
  }

  @Override
  public void addListener(SHandlerFunc h, String path) throws SException {
    root.addListener(h, path);
  }


  @Override
  public void removeListener(SHandlerFunc h, String path) throws SException {
    root.removeListener(h, path);
  }


  @Override
  public String getId() {
    return waveManager.getId();
  }

  @Override
  public void addParticipant(String participantId) throws InvalidParticipantAddress {
    waveManager.addParticipant(participantId);
  }

  @Override
  public void removeParticipant(String participantId) throws InvalidParticipantAddress {
    waveManager.removeParticipant(participantId);
  }

  @Override
  public String[] getParticipants() {
    return getParticipants();
  }


  @Override
  public void setPublic(boolean isPublic) {
    waveManager.setPublic(isPublic);
  }

  public boolean isPublic() {
    return waveManager.isPublic();
  }

  public Object js() {
    return new Proxy(root, new SMapProxyHandler());
  }

  @Override
  public void setStatusHandler(StatusHandler h) {
    this.statusHandler = h;
  }

  public void onStatusEvent(SStatusEvent e) {
    if (statusHandler != null)
      statusHandler.exec(e);
  }

  //
  // SMap interface
  //


  @Override
  public SNode pick(String key) throws SException {
    return root.pick(key);
  }

  @Override
  public SMap put(String key, SNode value) throws SException {
    return root.put(key, value);
  }

  @Override
  public SMap put(String key, Object object) throws SException {
    return root.put(key, object);
  }

  @Override
  public void remove(String key) throws SException {
    root.remove(key);
  }

  @Override
  public boolean has(String key) throws SException {
    return root.has(key);
  }

  @Override
  public String[] keys() throws SException {
    return root.keys();
  }

  @Override
  public void clear() throws SException {
    root.clear();
  }

  @Override
  public boolean isEmpty() {
    return root.isEmpty();
  }

  @Override
  public int size() {
    return root.size();
  }


	@Override
	public String[] _getBlips() {
    return waveManager.getBlips();
	}

	@Override
  public String _getBlipXML(String blipId) {
    return waveManager.getBlipXML(blipId);
	}

  @Override
  public SMap getUserObject() {
    return waveManager.getUserObject();
  }

  public boolean isNew() {
    return false;
  }

  @SuppressWarnings("rawtypes")
  @JsIgnore
  @Override
  public void accept(SVisitor visitor) {
    visitor.visit(this);
  }

  //
  // -----------------------------------------------------
  //

  @Override
  public void set(String path, Object value) {
    SNode.set(this, path, value);
  }

  @Override
  public void push(String path, Object value, @JsOptional Object index) {
    SNode.push(this, path, value, index);
  }

  @Override
  public Object pop(String path) {
    return SNode.pop(this, path);
  }

  @Override
  public int length(String path) {
    return SNode.length(this, path);
  }

  @Override
  public boolean contains(String path, String property) {
    return SNode.contains(this, path, property);
  }

  @Override
  public void delete(String path) {
    SNode.delete(this, path);
  }

  @Override
  public Object get(String path) {
    return SNode.get(this, path);
  }

  @Override
  public SNode node(String path) throws SException {
    return SNode.node(this, path);
  }

}