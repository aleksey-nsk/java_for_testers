package ru.stqa.pft.sandbox;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PrimeTests {

  @Test
  public void testPrime() {
    System.out.println("\n***** testPrime() *****");
    Assert.assertTrue(Prime.isPrime(Integer.MAX_VALUE));
  }

  @Test
  public void testPrimeFast() {
    System.out.println("\n***** testPrimeFast() *****");
    Assert.assertTrue(Prime.isPrimeFast(Integer.MAX_VALUE));
  }

  @Test(enabled = false) // отключили данный тест
  public void testPrimeLong() {
    System.out.println("\n***** testPrimeLong() *****");
    long n = Integer.MAX_VALUE;
    Assert.assertTrue(Prime.isPrime(n));
  }

  @Test
  public void testNotPrime() {
    System.out.println("\n***** testNotPrime() *****");
    Assert.assertFalse(Prime.isPrime(10));
    Assert.assertFalse(Prime.isPrimeWhile(22));
  }
}
