# The Supermarket Receipt Refactoring Kata

The supermarket has a catalog with different types of products (rice, apples, milk, toothbrushes,...). Each product has a price, and the total price of the shopping cart is the total of all the prices of the items. You get a receipt that details the items you've bought, the total price and any discounts that were applied.

The supermarket runs special deals, e.g.
 - Buy two toothbrushes, get one free. Normal toothbrush price is €0.99
 - 20% discount on apples, normal price €1.99 per kilo.
 - 10% discount on rice, normal price €2.49 per bag
 - Five tubes of toothpaste for €7.49, normal price €1.79
 - Two boxes of cherry tomatoes for €0.99, normal price €0.69 per box.

These are just examples: the actual special deals change each week.

## New feature: special deals database

Business intelligence wants to optimize our special deals to improve revenue.
At the moment they are based on long years of experience and are pretty much
hard coded. They want to organize the offers in a database.
Our sysadmin will take care of the database but you should prepare the codebase
for the change coming so we can connect the systems once the database goes online.

## New feature: HTML receipt

Currently we print a traditional ticket receipt. Now being a modern business we'd
like to be able to print or send a HTML version of the same receipt. All the data
and number formatting should be the same. However the layout should be html.
You don't have to worry about the HTML template - a designer will care of that - but
we do need someone to keep duplication between the reports to a bare minimum.

## New feature: discounted bundles

The owner of the system has a new feature request. They want to introduce a new kind of special offer - bundles. When you buy all the items in a product bundle
you get 10% off the total for those items. For example you could make a bundle offer of one toothbrush and one toothpaste. If you then you buy one toothbrush and one toothpaste, the discount will be 10% of €0.99 + €1.79. If you instead buy two toothbrushes and one toothpaste, you get the same discount as if you'd bought only one of each - ie only complete bundles are discounted.

---

This kata was slightly modified for the purpose of the coderetreat.
