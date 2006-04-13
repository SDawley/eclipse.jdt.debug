/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.debug.ui.variables;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.debug.internal.ui.viewers.provisional.IColumnPresentation;
import org.eclipse.debug.internal.ui.viewers.provisional.IColumnPresentationFactoryAdapter;
import org.eclipse.debug.internal.ui.viewers.provisional.IPresentationContext;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.jdt.debug.core.IJavaStackFrame;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;

/**
 * @since 3.2
 *
 */
public class JavaVariableColumnPresentationFactory implements IColumnPresentationFactoryAdapter {

	/* (non-Javadoc)
	 * @see org.eclipse.debug.internal.ui.viewers.provisional.IColumnPresentationFactoryAdapter#createColumnPresentation(org.eclipse.debug.internal.ui.viewers.provisional.IPresentationContext, java.lang.Object)
	 */
	public IColumnPresentation createColumnPresentation(IPresentationContext context, Object element) {
		if (isApplicable(context, element)) {
			return new JavaVariableColumnPresentation();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.internal.ui.viewers.provisional.IColumnPresentationFactoryAdapter#getColumnPresentationId(org.eclipse.debug.internal.ui.viewers.provisional.IPresentationContext, java.lang.Object)
	 */
	public String getColumnPresentationId(IPresentationContext context, Object element) {
		if (isApplicable(context, element)) {
			return JavaVariableColumnPresentation.JAVA_VARIABLE_COLUMN_PRESENTATION;
		}
		return null;
	}
	
	private boolean isApplicable(IPresentationContext context, Object element) {
		IWorkbenchPart part = context.getPart();
		IJavaStackFrame frame = null;
		if (part != null) {
			IWorkbenchPartSite site = part.getSite();
			if (site != null) {
				if (IDebugUIConstants.ID_VARIABLE_VIEW.equals(site.getId())) {
					if (element instanceof IAdaptable) {
						IAdaptable adaptable = (IAdaptable)element;
						frame = (IJavaStackFrame) adaptable.getAdapter(IJavaStackFrame.class);
					}
				}
			}
		}
		return frame != null;		
	}

}