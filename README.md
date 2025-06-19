This is a backend application for a Grocery Shop E-Commerce System developed using Spring Boot, MySQL, and Thymeleaf. It supports multiple user roles including Admin, Seller, and Customer.

✅ User Roles

ADMIN: Full access — manage products, users, orders

SELLER: Add, update, and delete their own products

CUSTOMER: Place orders, view their order history

✅ Authentication

Login with session-based tracking

Role-based access control (without Spring Security)

✅ Product Management

Add, update, delete (only by seller or admin)

View all products

✅ Order Management

Customers can place orders

Order stock is auto-managed

Admins can update/delete any order

Customers can view only their orders

✅ Validation & Exception Handling

DTO-based validation

Stock checks before placing orders

Custom error messages returned in Postman

✅ Swagger API Documentation

Integrated OpenAPI 3.0

Try all endpoints directly from Swagger UI
