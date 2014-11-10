import com.github.rnowling.bps.datagenerator.framework.samplers.MonteCarloSampler
import com.github.rnowling.bps.datagenerator.framework.pdfs.ExponentialPDF
import com.github.rnowling.bps.datagenerator.framework.samplers.UniformSampler
import com.github.rnowling.bps.datagenerator.framework.SeedFactory

averageValue = 2.0

seedFactory = new SeedFactory()
uniformSampler = new UniformSampler(0.0, 100.0, seedFactory)
pdf = new ExponentialPDF(1.0 / averageValue)


mcSampler = new MonteCarloSampler(uniformSampler, pdf, seedFactory)

sample = mcSampler.sample()

println("Sampled the value: " + sample)

sampleSum = 0.0
for(int i = 0; i < 10000; i++)
{
	sampleSum += mcSampler.sample()
}

sampleAverage = sampleSum / 10000.0

println("Expected Average: " + averageValue)
println("Observed Average: " + sampleAverage)

