// Copyright 2008-2010 Victor Iacoban
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software distributed under
// the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
// either express or implied. See the License for the specific language governing permissions and
// limitations under the License.
package org.zmlx.hg4idea;

import com.intellij.dvcs.branch.DvcsBranchSync;
import com.intellij.dvcs.branch.DvcsSyncBranchSettings;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.changes.VcsAnnotationRefresher;
import org.jetbrains.annotations.NotNull;

@State(
  name = "hg4idea.settings",
  storages = @Storage(file = StoragePathMacros.WORKSPACE_FILE)
)
public class HgProjectSettings implements PersistentStateComponent<HgProjectSettings.State>, DvcsSyncBranchSettings {

  @NotNull private final HgGlobalSettings myAppSettings;
  @NotNull private final Project myProject;

  private State myState = new State();

  public HgProjectSettings(@NotNull Project project, @NotNull HgGlobalSettings appSettings) {
    myProject = project;
    myAppSettings = appSettings;
  }

  public static class State {

    public boolean myCheckIncoming = true;
    public boolean myCheckOutgoing = true;
    public Boolean CHECK_INCOMING_OUTGOING = null;
    public boolean myIgnoreWhitespacesInAnnotations = true;
    public DvcsBranchSync SYNC_SETTING = DvcsBranchSync.NOT_DECIDED;
  }

  public State getState() {
    return myState;
  }

  public void loadState(State state) {
    myState = state;
    if (state.CHECK_INCOMING_OUTGOING == null) {
      state.CHECK_INCOMING_OUTGOING = state.myCheckIncoming || state.myCheckOutgoing;
    }
  }

  public boolean isCheckIncomingOutgoing() {
    return myState.CHECK_INCOMING_OUTGOING != null && myState.CHECK_INCOMING_OUTGOING.booleanValue();
  }

  public boolean isWhitespacesIgnoredInAnnotations() {
    return myState.myIgnoreWhitespacesInAnnotations;
  }

  @NotNull
  public DvcsBranchSync getSyncSetting() {
    return myState.SYNC_SETTING;
  }

  public void setSyncSetting(@NotNull DvcsBranchSync syncSetting) {
    myState.SYNC_SETTING = syncSetting;
  }

  public void setCheckIncomingOutgoing(boolean checkIncomingOutgoing) {
    myState.CHECK_INCOMING_OUTGOING = checkIncomingOutgoing;
  }

  public void setIgnoreWhitespacesInAnnotations(boolean ignoreWhitespacesInAnnotations) {
    if (myState.myIgnoreWhitespacesInAnnotations != ignoreWhitespacesInAnnotations) {
      myState.myIgnoreWhitespacesInAnnotations = ignoreWhitespacesInAnnotations;
      myProject.getMessageBus().syncPublisher(VcsAnnotationRefresher.LOCAL_CHANGES_CHANGED).configurationChanged(HgVcs.getKey());
    }
  }

  public String getHgExecutable() {
    return myAppSettings.getHgExecutable();
  }

  public void setHgExecutable(String text) {
    myAppSettings.setHgExecutable(text);
  }

  @NotNull
  public HgGlobalSettings getGlobalSettings() {
    return myAppSettings;
  }
}
