# Baby And Mom

![Baby and Mom Screenshot](./images/Homepage.png)

## Introduction
Welcome to Koi Farm Shop, a comprehensive management platform tailored for koi fish enthusiasts and businesses. 
Whether you're looking to consign, purchase, sell, or care for koi fish, our platform provides the tools and features to meet your needs seamlessly.

## Features
Consignment Services: Safely list your koi fish for sale through our platform.
Fish Care Management: Professional care options for your koi, including health monitoring and maintenance services.
Buy & Sell Marketplace: A reliable and transparent marketplace for buying and selling koi fish.

![Koi Shop Screenshot](./images/KoiShop.png)

Customer-Centric Services: Tailored support for customers, ensuring satisfaction at every step.

## Tech Stack
- **Backend**: ASP.Net, Swagger

- **Database**: SQL

- **Authentication**: JWT for secure user management.

- **Payment**: VnPay

- **Other**: Firebase, Gmail

## Architecture: Dependency Injection (DI)
In Koi Farm Shop, we utilize Dependency Injection (DI) as a core design pattern to improve maintainability, flexibility, and testability of the application. DI allows for the decoupling of components, making the system modular and easier to scale.

### How DI Works
DI is implemented to inject services and dependencies into classes, allowing for greater flexibility and ease of testing. For example, the FishService class in our system is injected with a FishRepository dependency, instead of directly creating the repository object.

### Benefits
Loose Coupling: Components are not tightly bound to each other, which simplifies maintenance and testing.
Testability: Easy to substitute mock implementations during testing.
Scalability: New services and components can be added without affecting others.

## Contribution
The Koi Farm Shop platform is proudly built and maintained by a dedicated team of four contributors:

1. **[Minh](https://github.com/pq-minh)** – Project Manager, Backend Developer
2. **[Hoàng](https://github.com/VHoangSE)** –  Backend Developer
3. **[Duy](https://github.com/ChickenCode-FPT)** – Backend Developer: Login and Consignment service for selling fish on the system, Frontend Developer
4. **[Danh](https://github.com/danh-pham235)** – Frontend Developer

We thank each contributor for their hard work and dedication to making this project a success!

## Frontend Repository  
For the frontend code of the Koi Farm Shop platform, please visit the [Frontend GitHub Repository](https://github.com/pq-minh/KoiFarmShopFE).

This repository contains all the client-side code, including the user interface, components, and interactions with the backend.
