/*******************************************************************************
 *  Copyright (c) 2000, 2009 IBM Corporation and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 * 
 *  Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.debug.tests.launching;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.internal.ui.DebugUIPlugin;
import org.eclipse.debug.ui.RefreshTab;
import org.eclipse.jdt.debug.tests.AbstractDebugTest;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;

/**
 * Tests the refresh tab.
 */
public class RefreshTabTests extends AbstractDebugTest {
	
	/**
	 * Constructor
	 * @param name
	 */
	public RefreshTabTests(String name) {
		super(name);
	}

	/**
	 * Sets the selected resource in the navigator view.
	 * 
	 * @param resource resource to select
	 */
	protected void setSelection(final IResource resource) {
		Runnable r = new Runnable() {
			public void run() {
				IWorkbenchPage page = DebugUIPlugin.getActiveWorkbenchWindow().getActivePage();
				assertNotNull("The active workbench page should not be null", page);
				IViewPart part;
				try {
					part = page.showView("org.eclipse.ui.views.ResourceNavigator"); //$NON-NLS-1$
					IWorkbenchPartSite site = part.getSite();
					assertNotNull("The part site for org.eclipse.ui.views.ResourceNavigator should not be null ", site);
					ISelectionProvider provider = site.getSelectionProvider();
					assertNotNull("the selection provider should not be null for org.eclipse.ui.views.ResourceNavigator", provider);
					provider.setSelection(new StructuredSelection(resource));
				} catch (PartInitException e) {
					assertNotNull("Failed to open navigator view", null); //$NON-NLS-1$
				}
				
			}
		};
		DebugUIPlugin.getStandardDisplay().syncExec(r);
	}
	
	/**
	 * Tests a refresh scope of the selected resource
	 * @throws CoreException
	 */
	public void testSelectedResource() throws CoreException {
		String scope = "${resource}"; //$NON-NLS-1$
		IResource resource = getJavaProject().getProject().getFolder("src"); //$NON-NLS-1$
		setSelection(resource);
		IResource[] result = RefreshTab.getRefreshResources(scope);
		assertNotNull(result);
		assertEquals(1, result.length);
		assertEquals(resource, result[0]);		
	}
	
	/**
	 * Tests a refresh scope of the selected resource's container
	 * @throws CoreException
	 */
	public void testSelectionsFolder() throws CoreException {
		String scope = "${container}"; //$NON-NLS-1$
		IResource resource = getJavaProject().getProject().getFolder("src"); //$NON-NLS-1$
		setSelection(resource);
		IResource[] result = RefreshTab.getRefreshResources(scope);
		assertNotNull(result);
		assertEquals(1, result.length);
		assertEquals(resource.getParent(), result[0]);		
	}
	
	/**
	 * Tests a refresh scope of the selected resource's project
	 * @throws CoreException
	 */
	public void testSelectionsProject() throws CoreException {
		String scope = "${project}"; //$NON-NLS-1$
		IResource resource = getJavaProject().getProject().getFolder("src"); //$NON-NLS-1$
		setSelection(resource);
		IResource[] result = RefreshTab.getRefreshResources(scope);
		assertNotNull(result);
		assertEquals(1, result.length);
		assertEquals(resource.getProject(), result[0]);		
	}	
	
	/**
	 * Tests a refresh scope of the selected resource's project
	 * @throws CoreException
	 */
	public void testWorkspaceScope() throws CoreException {
		String scope = "${workspace}"; //$NON-NLS-1$
		IResource[] result = RefreshTab.getRefreshResources(scope);
		assertNotNull(result);
		assertEquals(1, result.length);
		assertEquals(ResourcesPlugin.getWorkspace().getRoot(), result[0]);		
	}	
	
	/**
	 * Tests a refresh scope for a specific resource (old format)
	 * @throws CoreException
	 */
	public void testSpecificResource() throws CoreException {
		String scope = "${resource:/DebugTests/.classpath}"; //$NON-NLS-1$
		IResource resource = getJavaProject().getProject().getFile(".classpath"); //$NON-NLS-1$
		IResource[] result = RefreshTab.getRefreshResources(scope);
		assertNotNull(result);
		assertEquals(1, result.length);
		assertEquals(resource, result[0]);				
	}
	
	/**
	 * Tests a refresh scope for a working set
	 * @throws CoreException
	 */
	public void testWorkingSet() throws CoreException {
		String scope= "${working_set:<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<launchConfigurationWorkingSet factoryID=\"org.eclipse.ui.internal.WorkingSetFactory\" name=\"workingSet\" editPageId=\"org.eclipse.ui.resourceWorkingSetPage\">\n<item factoryID=\"org.eclipse.ui.internal.model.ResourceFactory\" path=\"/DebugTests/.classpath\" type=\"1\"/>\n</launchConfigurationWorkingSet>}"; //$NON-NLS-1$
		IResource resource = getJavaProject().getProject().getFile(".classpath"); //$NON-NLS-1$
		IResource[] result = RefreshTab.getRefreshResources(scope);
		assertNotNull(result);
		assertEquals(1, result.length);
		assertEquals(resource, result[0]);			
	}
}