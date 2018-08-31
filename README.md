# rocinante

Test suite for integration tests

![Rocinante Horse](http://www.artnet.com/WebServices/images/ll00162lldQkOGFgVeECfDrCWvaHBOcZVDC/oskar-garvens-don-quixote-de-la-mancha-upon-his-horse-rocinante.jpg)

> **Rocinante** is [Don Quixote](https://en.wikipedia.org/wiki/Don_Quixote)'s donkey in the novel Don Quixote by _Miguel de Cervantes_. In many ways, Rocinante is not only Don Quixote's horse, but also his double: like Don Quixote, he is awkward, past his prime, and engaged in a task beyond his capacities.

Rocinante project is basically a [Spock](http://spockframework.org) extension that reads a mapping's tape directory and generate dynamic feature methods [Wiremock](http://wiremock.org/docs/record-playback) based.

`@Rocinante`: Field's annotation binder for mapping files

- `binding`: binding value for mapping file;
- `config`: config value for configurations (binding precedence);
- `isFile`: flag that indicates the binding is a file pointer (default `false`);
- `condition`: closure condition for create a dynamic feature method (delegate is the mapping file);

`RocinanteExtension`: A Spock extension where the stuffs happen.

`RocinanteInterceptor`: A Spock interceptor that writes a field with `@Rocinante` annotation binder.

Config properties (Java properties):

```properties
host=http://www.mocky.io
basepath=src/test/resources
mappings=/mappings
tapes=/__files
```

## Built-in Specifications

- **DifferenceURLContentSpec**: Spec does diff between mapping response body and real path response body (json diff compare). Only responses with status 200 are considerate in condition clause.

# How to Run

```bash
./gradlew test
```

You can use `-D<property>` for overriding default properties.

# How to Build

It's not necessary you build the project. Just use =]

# How to Deploy

Rocinante is a tool/lib project and the deploy is subjective according to the project that uses.
