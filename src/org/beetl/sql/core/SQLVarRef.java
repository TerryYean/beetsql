package org.beetl.sql.core;

import java.lang.reflect.Method;

import org.beetl.core.Context;
import org.beetl.core.exception.BeetlException;
import org.beetl.core.statement.VarAttribute;
import org.beetl.core.statement.VarRef;

public class SQLVarRef extends VarRef {
	public SQLVarRef(VarRef ref) {
		super(ref.attributes, ref.hasSafe, ref.safe, ref.token, ref.token);

	}

	public Object evaluate(Context ctx) {

		Object value = ctx.vars[varIndex];
		if (value == Context.NOT_EXIST_OBJECT) {
			// check root
			Object o = ctx.getGlobal("_root");
			if (o == null) {
				return super.evaluate(ctx);
			} else {
				try {
					String attr = this.token.text;
					String getter = "get" + attr.substring(0, 1).toUpperCase() + attr.substring(1);
					Method method = o.getClass().getMethod(getter, new Class[] {});
					Object realValue = method.invoke(o, new Object[] {});
					ctx.vars[varIndex] = realValue;
					return super.evaluate(ctx);
				} catch (Exception e) {
					BeetlException ex = new BeetlException(BeetlException.VAR_NOT_DEFINED, e.getMessage());
					ex.pushToken(this.token);
					throw ex;
				}

			}
		}
		return super.evaluate(ctx);
	}
}
