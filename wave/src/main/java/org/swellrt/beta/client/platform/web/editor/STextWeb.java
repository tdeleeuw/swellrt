package org.swellrt.beta.client.platform.web.editor;


import org.swellrt.beta.client.platform.web.editor.caret.CaretInfo;
import org.swellrt.beta.common.SException;
import org.swellrt.beta.model.SMap;
import org.waveprotocol.wave.client.common.util.LogicalPanel;
import org.waveprotocol.wave.client.editor.content.ContentDocument;
import org.waveprotocol.wave.client.wave.InteractiveDocument;

import com.google.gwt.dom.client.Element;

import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsType;

@JsType(namespace = "swell", name = "TextWeb")
public interface STextWeb {

  public void setInteractive() throws SException;

  @JsIgnore
  public void setInteractive(LogicalPanel.Impl panel) throws SException;

  public void setParent(Element element) throws SException;

  public void setRendered();

  public void setShelved();

  public boolean isEmpty();

  public SRange insert(SRange at, String content);

  public SRange replace(SRange at, String content);

  @JsIgnore
  public InteractiveDocument getInteractiveDocument();

  @JsIgnore
  public ContentDocument getContentDocument();

  public void showDiffHighlight();

  public void hideDiffHighlight();

  /**
   * A map storing pairs of (sessionId, {@link CaretInfo}) that must be updated
   * by editors.
   *
   * @return a map of caret infos.
   */
  public SMap getLiveCarets();

}
