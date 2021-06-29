package metrics.multiple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.rng.RandomProviderState;

import game.Game;
import main.math.LinearRegression;
import metrics.Metric;
import metrics.Utils;
import other.concept.Concept;
import other.context.Context;
import other.trial.Trial;

public abstract class MultiMetricFramework extends Metric
{

	public enum MultiMetricValue 
	{
		Average,
		Median,
		Max,
		Min,
		Variance,
		
		ChangeAverage,
		ChangeSign,
		ChangeLineBestFit,
		ChangeNumTimes,
		
		MaxIncrease,
		MaxDecrease
	}
	
	//-------------------------------------------------------------------------
	
	public MultiMetricFramework
	(
		final String name, final String notes, final double min, final double max, 
		final Concept concept, final MultiMetricValue multiMetricValue
	) 
	{
		super(name, notes, min, max, concept, multiMetricValue);
	}
	
	//-------------------------------------------------------------------------
	
	public abstract Double[] getMetricValueList(final Trial trial, final Context context); 
	
	//-------------------------------------------------------------------------
	
	public Double[][] getMetricValueLists(final Game game, final Trial[] trials, final RandomProviderState[] randomProviderStates)
	{
		final ArrayList<Double[]> metricValueLists = new ArrayList<>();
		
		for (int trialIndex = 0; trialIndex < trials.length; trialIndex++)
		{
			final Trial trial = trials[trialIndex];
			final RandomProviderState rngState = randomProviderStates[trialIndex];
			final Context context = Utils.setupNewContext(game, rngState);
			metricValueLists.add(getMetricValueList(trial, context));
		}

		return metricValueLists.toArray(new Double[0][0]);
	}
	
	//-------------------------------------------------------------------------
	
	public double metricAverage(final Double[][] metricValues)
	{
		double metricAverageFinal = 0.0;
		for (final Double[] valueList : metricValues)
		{
			double metricAverage = 0.0;
			for (final Double value : valueList)
				metricAverage += value.doubleValue() / valueList.length;

			metricAverageFinal += metricAverage;
		}
		return metricAverageFinal / metricValues.length;
	}
	
	public double metricMedian(final Double[][] metricValues)
	{
		double metricMedianFinal = 0.0;
		for (final Double[] valueList : metricValues)
		{
			Arrays.sort(valueList);
			final double metricMedian = valueList[valueList.length/2];
			metricMedianFinal += metricMedian;
		}
		return metricMedianFinal / metricValues.length;
	}
	
	public double metricMax(final Double[][] metricValues)
	{
		double metricMaxFinal = 0.0;
		for (final Double[] valueList : metricValues)
		{
			double metricMax = 0.0;
			for (final Double value : valueList)
				metricMax = Math.max(metricMax, value.doubleValue());

			metricMaxFinal += metricMax;
		}
		return metricMaxFinal / metricValues.length;
	}
	
	public double metricMin(final Double[][] metricValues)
	{
		double metricMinFinal = 0.0;
		for (final Double[] valueList : metricValues)
		{
			double metricMin = 0.0;
			for (final Double value : valueList)
				metricMin = Math.min(metricMin, value.doubleValue());

			metricMinFinal += metricMin;
		}
		return metricMinFinal / metricValues.length;
	}
	
	public double metricVariance(final Double[][] metricValues)
	{
		double metricVarianceFinal = 0.0;
		for (final Double[] valueList : metricValues)
		{
			double metricAverage = 0.0;
			for (final Double value : valueList)
				metricAverage += value.doubleValue() / valueList.length;
			
			double metricVariance = 0.0;
			for (final Double value : valueList)
				metricVariance += Math.pow(value.doubleValue() - metricAverage, 2) / valueList.length;

			metricVarianceFinal += metricVariance;
		}
		return metricVarianceFinal / metricValues.length;
	}
	
	public double metricMaxIncrease(final Double[][] metricValues)
	{
		double metricMaxFinal = 0.0;
		for (final Double[] valueList : metricValues)
		{
			double metricMax = 0.0;
			double lastValue = valueList[0].doubleValue();
			for (final Double value : valueList)
			{
				final double change = value.doubleValue() - lastValue;
				metricMax = Math.max(metricMax, change);
				lastValue = value.doubleValue();
			}
			metricMaxFinal += metricMax;
		}
		return metricMaxFinal / metricValues.length;
	}
	
	public double metricMaxDecrease(final Double[][] metricValues)
	{
		double metricMaxFinal = 0.0;
		for (final Double[] valueList : metricValues)
		{
			double metricMax = 0.0;
			double lastValue = valueList[0].doubleValue();
			for (final Double value : valueList)
			{
				final double change = value.doubleValue() - lastValue;
				metricMax = Math.min(metricMax, change);
				lastValue = value.doubleValue();
			}
			metricMaxFinal += metricMax;
		}
		return metricMaxFinal / metricValues.length;
	}
	
	/** The slope of the least squares line of best fit. */
	public double metricChangeLineBestFit(final Double[][] metricValues)
	{
		double metricChangeFinal = 0.0;
		for (final Double[] valueList : metricValues)
		{
			final double[] xAxis = IntStream.range(0, valueList.length).asDoubleStream().toArray();
			final double[] yAxis = Stream.of(valueList).mapToDouble(Double::doubleValue).toArray();
			final LinearRegression linearRegression = new LinearRegression(xAxis, yAxis);

			metricChangeFinal += linearRegression.slope();
		}
		return metricChangeFinal / metricValues.length;
	}
	
	/** The average increase */
	public double metricChangeAverage(final Double[][] metricValues)
	{
		double metricChangeFinal = 0.0;
		for (final Double[] valueList : metricValues)
		{
//			double metricChange = 0.0;
//			double lastValue = valueList[0].doubleValue();
//			for (final Double value : valueList)
//			{
//				final double change = value.doubleValue() - lastValue;
//				metricChange += change / (valueList.length-1);
//				lastValue = value.doubleValue();
//			}
			
			final double firstValue = valueList[0].doubleValue();
			final double lastValue = valueList[valueList.length-1].doubleValue();
			final double metricChange = (lastValue - firstValue) / (valueList.length-1);
			metricChangeFinal += metricChange;
		}
		return metricChangeFinal / metricValues.length;
	}
	
	/** The average number of times the value increased versus decreased. */
	public double metricChangeSign(final Double[][] metricValues)
	{
		double metricChangeFinal = 0.0;
		for (final Double[] valueList : metricValues)
		{
			double metricChange = 0.0;
			double lastValue = valueList[0].doubleValue();
			for (final Double value : valueList)
			{
				double change = value.doubleValue() - lastValue;
				if (change > 0)
					change = 1;
				else if (change < 0)
					change = -1;
				else
					change = 0;
				metricChange += change / (valueList.length-1);
				lastValue = value.doubleValue();
			}
			
			metricChangeFinal += metricChange;
		}
		return metricChangeFinal / metricValues.length;
	}
	
	/** The average number of times the direction changed. */
	public double metricChangeNumTimes(final Double[][] metricValues)
	{
		double metricChangeFinal = 0.0;
		for (final Double[] valueList : metricValues)
		{
			double metricChange = 0.0;
			double valueChangeDirection = 0.0;	// If 1.0 then increasing, if -1.0 then decreasing
			double lastValue = valueList[0].doubleValue();
			
			for (final Double value : valueList)
			{
				double direction = 0.0;
				if (value > lastValue)
					direction = 1.0;
				if (value < lastValue)
					direction = -1.0;
				if (direction != 0.0 && valueChangeDirection != direction)
					metricChange++;
				valueChangeDirection = direction;
				
				lastValue = value.doubleValue();
			}
			
			metricChangeFinal += metricChange / (valueList.length-1);
		}
		return metricChangeFinal / metricValues.length;
	}
	
	//-------------------------------------------------------------------------
	
	@Override
	public double apply
	(
			final Game game,
			final Trial[] trials,
			final RandomProviderState[] randomProviderStates
	)
	{
		final Double[][] metricValues = getMetricValueLists(game, trials, randomProviderStates);
		
		switch (multiMetricValue())
		{
			case Average: return metricAverage(metricValues);
			case Median: return metricMedian(metricValues);
			case Max: return metricMax(metricValues);
			case Min: return metricMin(metricValues);
			case Variance: return metricVariance(metricValues);
			
			case ChangeAverage: return metricChangeAverage(metricValues);
			case ChangeSign: return metricChangeSign(metricValues);
			case ChangeLineBestFit: return metricChangeLineBestFit(metricValues);
			case ChangeNumTimes: return metricChangeNumTimes(metricValues);
			
			case MaxIncrease: return metricMaxIncrease(metricValues);
			case MaxDecrease: return metricMaxDecrease(metricValues);
			
			default: return -1;
		}
	}
	
}
