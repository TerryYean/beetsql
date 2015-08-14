package org.beetl.sql.core.engine;

import java.io.IOException;
import java.util.List;

import org.beetl.core.Context;
import org.beetl.core.InferContext;
import org.beetl.core.exception.BeetlException;
import org.beetl.core.statement.Expression;
import org.beetl.core.statement.FormatExpression;
import org.beetl.core.statement.FunctionExpression;
import org.beetl.core.statement.PlaceholderST;
import org.beetl.core.statement.Statement;
import org.beetl.core.statement.Type;

public class SQLPlaceholderST extends Statement
{
	public Expression expression;
	public Type type = null;
	FormatExpression format;

	public SQLPlaceholderST(PlaceholderST st)
	{
		super(st.token);
		this.type = st.type;
		this.expression = st.expression;

	}

	@Override
	public final void execute(Context ctx)
	{
		try{
			Object value = expression.evaluate(ctx);
			if(expression instanceof FunctionExpression){
				FunctionExpression fun = (FunctionExpression)expression;
				if(fun.token.text.equals("text")){
					ctx.byteWriter.writeString(value!=null?value.toString():"");
					return ;
				}
			}
			
			ctx.byteWriter.writeString("?");
			List list = (List)ctx.getGlobal("_paras");
			list.add(value);
			
		}
		catch (IOException e)
		{
			BeetlException be = new BeetlException(BeetlException.CLIENT_IO_ERROR_ERROR, e.getMessage(), e);
			be.pushToken(this.token);
			throw be;
		}

	}

	@Override
	public void infer(InferContext inferCtx)
	{
		expression.infer(inferCtx);
		this.type = expression.type;
	}

}