import com.github.rnowling.bps.datagenerator.framework.samplers.MonteCarloSampler
import com.github.rnowling.bps.datagenerator.framework.pdfs.GaussianPDF
import com.github.rnowling.bps.datagenerator.framework.samplers.UniformSampler
import com.github.rnowling.bps.datagenerator.framework.SeedFactory

averageValue = 10.0
stdValue = 2.0

seedFactory = new SeedFactory()
uniformSampler = new UniformSampler(-100.0, 100.0, seedFactory)
pdf = new GaussianPDF(averageValue, stdValue)


mcSampler = new MonteCarloSampler(uniformSampler, pdf, seedFactory)

sample = mcSampler.sample()

println("Sampled the value: " + sample)

sampleSum = 0.0
for(int i = 0; i < 100000; i++)
{
	sampleSum += mcSampler.sample()
}

sampleAverage = sampleSum / 100000.0

println("Expected Average: " + averageValue)
println("Observed Average: " + sampleAverage)

