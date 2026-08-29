# Capability Bus — TODO

## v0.x — Toy PoC (Alpha ↔ Beta)

### Done
- [x] Two independent Quarkus services (service-a: 8081, service-b: 8080)
- [x] Basic REST endpoints on each service
- [x] Virtual threads on REST endpoints (`@RunOnVirtualThread`)
- [x] Service A → Service B direct call via MicroProfile REST Client (later removed)
- [x] Kafka wired into both services (`quarkus-messaging-kafka`)
- [x] Shared `BusMessage` record (`cid`, `capability`, `needReply`, `payload`)
- [x] JSON (de)serialization via Quarkus `ObjectMapperSerializer`/`ObjectMapperDeserializer`
- [x] Service A publishes commands to `commands-topic`
- [x] Service B: local partial registry (`Map<String, Handler>`) + dispatch by capability string
- [x] Service B: `v1:ADD_LIKE` handler implemented
- [x] Correlation ID (`cid`) generated on publish, carried through to reply
- [x] Service B publishes reply to `replies-topic` (only when `needReply` is true)
- [x] Service A: reply consumer (`ReplyConsumer`) logs incoming replies by `cid`
- [x] End-to-end round trip confirmed working (publish → dispatch → reply → received)
- [x] Local Kafka running (ZooKeeper-based) for dev/testing
- [x] Refactor `CommandDispatcher`: move `needReply` branch out of individual handlers into the dispatcher itself (handlers just return a value or null)


### Remaining
- [ ] Add a second capability to prove the registry pattern generalizes past one entry
- [ ] Handle unknown/unregistered capability cleanly (currently just logs and drops)
- [ ] Decide + implement error handling for handler exceptions (currently unhandled)
- [ ] Redis-backed pending-state for correlation IDs (per original design doc, Phase 3) — currently replies are just logged, not matched against a pending-request store
- [ ] Load testing (k6 for HTTP-level, or kafka-producer-perf-test for raw broker throughput)
- [ ] Decide on retry/timeout behavior for commands that never get a reply

## Deferred (explicitly, not forgotten — later phases / v2+)
- [ ] Centralized capability registry as its own service (vs current local partial registry)
- [ ] Push-style routing (registry actively forwards messages) — deferred in favor of pull/self-select
- [ ] A/B / canary routing between capability versions
- [ ] Reflection/annotation-based command registration (`@Command(name=...)`) — built only as a documented alternative example, not the main implementation
- [ ] Schema registry / payload versioning (Phase 6–7 of original design doc)
- [ ] Failure-injection / chaos experiments (Phase 9 of original design doc)
- [ ] Sidecar pattern for polyglot language support

## White-paper / PoC artifacts (bigger picture)
- [ ] Refine architecture white-paper with lessons learned from the toy build
- [ ] Stress-test demo app (likes/comments/reactions-style, high-throughput, batched DB writes every ~30s) to generate real performance numbers for the whitepaper
- [ ] Two registry example repos: simple map-based vs reflective/annotation-based, with an explicit tradeoff writeup (when to use which)