/*
 * (c) Copyright IBM Corp. 2000, 2001, 2002.
 * All Rights Reserved.
 */
package org.eclipse.jdt.internal.debug.eval.ast.engine;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.debug.eval.ast.model.IPrimitiveValue;
import org.eclipse.jdt.debug.eval.ast.model.IValue;
import org.eclipse.jdt.debug.eval.ast.model.IVariable;

/**
 * @version 	1.0
 * @author
 */
public class AssignmentOperator extends CompoundInstruction {

	protected int fVariableTypeId;
	protected int fValueTypeId;


	public AssignmentOperator(int variableTypeId, int valueTypeId, int start) {
		super(start);
		fVariableTypeId = variableTypeId;
		fValueTypeId = valueTypeId;
	}

	/*
	 * @see Instruction#execute()
	 */
	public void execute() throws CoreException {
		IValue value = (IValue) popValue();
		IVariable variable = (IVariable) pop();
		
		if (value instanceof IPrimitiveValue) {
			IPrimitiveValue primitiveValue = (IPrimitiveValue) value;
			switch (fVariableTypeId) {
				case T_boolean:
					variable.setValue(newValue(primitiveValue.getBooleanValue()));
					break;
				case T_byte:
					variable.setValue(newValue(primitiveValue.getByteValue()));
					break;
				case T_short:
					variable.setValue(newValue(primitiveValue.getShortValue()));
					break;
				case T_char:
					variable.setValue(newValue(primitiveValue.getCharValue()));
					break;
				case T_int:
					variable.setValue(newValue(primitiveValue.getIntValue()));
					break;
				case T_long:
					variable.setValue(newValue(primitiveValue.getLongValue()));
					break;
				case T_float:
					variable.setValue(newValue(primitiveValue.getFloatValue()));
					break;
				case T_double:
					variable.setValue(newValue(primitiveValue.getDoubleValue()));
					break;
			}
		} else {
			variable.setValue(value);
		}
		push(variable.getValue());
	}

	public String toString() {
		return "'=' operator";
	}


}
