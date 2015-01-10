import com.github.rnowling.bps.datagenerator.DataLoader
import com.github.rnowling.bps.datagenerator.datamodels.inputs.*

loader = new DataLoader()
inputData = loader.loadData()

for(record in inputData.getZipcodeTable())
{
	System.out.println(record.getZipcode() + "," + 
	record.getCity() + "," + 
	record.getState() + "," + 
	record.getCoordinates().getFirst() + "," + 
	record.getCoordinates().getSecond())
}

