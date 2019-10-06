# Debate: Fluid API

## Unit

### Facts

- Vanilla says:
  - There are three bottles to a bucket. (cauldron)
  - One fluid block is equal to one bucket. (...buckets)
- The SME (Standardized Modding Environment) says:
  - There are one thousand millibuckets to a bucket. (Forge)
  - We'd like to be able to divide by 9\*9=81, to cover nugget -\>
    block liquid metals. (Tinkers' Construct fans)
  - We'd like our fluid to void as rarely as possible, preferably
    never.

### Approaches

- Choose a denominator\! (Shouldn't be too small, or too large.)
  - The Metric Option: 1000.
  - The Silk Option: 1620.
  - Millibottles: 3000 mb = 1000 mB = 3 bottles = 1 bucket.
  - Others: 900, 1800, 3600 (getting a bit high...)
- ~~Use BigDecimals\!~~ wait no that's still pretty metric
- Use rational numbers\!

## API

### Prior art

- [Silk's fluid API](https://github.com/Prospector/Silk/tree/master/src/main/java/io/github/prospector/silk/fluid)
  by Prospector

