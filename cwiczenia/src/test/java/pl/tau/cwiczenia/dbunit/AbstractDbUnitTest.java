package pl.tau.cwiczenia.dbunit;

import java.net.URL;

import org.dbunit.DBTestCase;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mssql.InsertIdentityOperation;
import org.junit.Test;

public abstract class AbstractDbUnitTest extends DBTestCase {

	protected static String url = "jdbc:hsqldb:hsql://localhost/workdb";
	
    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void setUp() throws Exception {
    	InsertIdentityOperation.TRUNCATE_TABLE.execute(getConnection(), getDataSet());
        InsertIdentityOperation.INSERT.execute(getConnection(), getDataSet());
    }
    
	protected IDataSet getDataSet(String datasetName) throws Exception {
        URL url = getClass().getClassLoader().getResource(datasetName);
        FlatXmlDataSet ret = new FlatXmlDataSetBuilder().build(url.openStream());
        return ret;
	}
}
