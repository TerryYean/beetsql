package org.beetl.sql.core;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

import org.beetl.core.Context;
import org.beetl.core.GroupTemplate;
import org.beetl.core.InferContext;
import org.beetl.core.Resource;
import org.beetl.core.engine.DefaultTemplateEngine;
import org.beetl.core.engine.StatementParser;
import org.beetl.core.exception.BeetlException;
import org.beetl.core.statement.Expression;
import org.beetl.core.statement.FormatExpression;
import org.beetl.core.statement.GeneralForStatement;
import org.beetl.core.statement.GrammarToken;
import org.beetl.core.statement.PlaceholderST;
import org.beetl.core.statement.Program;
import org.beetl.core.statement.Statement;
import org.beetl.core.statement.Type;
import org.beetl.core.statement.WhileStatement;

public class SQLTemplateEngine extends DefaultTemplateEngine
{
    public Program createProgram(Resource resource, Reader reader, Map<Integer, String> textMap, String cr,
                    GroupTemplate gt)
    {
            Program program = super.createProgram(resource, reader, textMap, cr, gt);
            modifyStatemetn(resource,program,gt);
            return program;
    }
    private void modifyStatemetn(Resource resource,Program program,GroupTemplate gt){
            Statement[] sts = program.metaData.statements;
            StatementParser parser = new StatementParser(sts, gt, resource.getId());
            parser.addListener(PlaceholderST.class, new PlaceHolderListener());
            parser.parse();
    }
    
    
}