# attentive-response

This library provides Ring middleware
`attentive-response.core/wrap-attentive-response`,
which automatically encodes response if its body is
a collection, depending on request header 'Accept'.

Currently supported content types are:

    - application/json
    - application/edn
    - application/transit+json

where `application/json` is default.

## License

Copyright © 2015 [Ilshad Khabibullin](http://ilshad.com).

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
