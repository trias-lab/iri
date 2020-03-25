package com.iota.iri.storage.localinmem;

import com.iota.iri.conf.BaseIotaConfig;
import com.iota.iri.conf.IotaConfig;
import com.iota.iri.conf.TestnetConfig;
import com.iota.iri.controllers.TransactionViewModel;
import com.iota.iri.model.Hash;
import com.iota.iri.storage.Tangle;
import com.iota.iri.storage.localinmemorygraph.LocalInMemoryGraphProvider;
import com.iota.iri.storage.rocksDB.RocksDBPersistenceProvider;
import com.iota.iri.utils.IotaUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.*;
import org.junit.rules.TemporaryFolder;
import org.neo4j.cypher.internal.frontend.v2_3.ast.functions.Has;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.iota.iri.controllers.TransactionViewModelTest.*;

public class LocalInMemoryGraphProviderTest {
    private static final TemporaryFolder dbFolder = new TemporaryFolder();
    private static final TemporaryFolder logFolder = new TemporaryFolder();
    private static final TemporaryFolder dbFolder1 = new TemporaryFolder();
    private static final TemporaryFolder logFolder1 = new TemporaryFolder();
    private static Tangle tangle1;
    private static LocalInMemoryGraphProvider provider1;

    @After
    public void tearDown() throws Exception {
        tangle1.shutdown();
        dbFolder.delete();
        BaseIotaConfig.getInstance().setStreamingGraphSupport(false);
    }

    @Before()
    public void setUp() throws Exception {
        BaseIotaConfig.getInstance().setStreamingGraphSupport(true);
        tangle1 = new Tangle();
        dbFolder.create();
        logFolder.create();
        dbFolder1.create();
        logFolder1.create();
        tangle1.addPersistenceProvider(new RocksDBPersistenceProvider(dbFolder.getRoot().getAbsolutePath(), logFolder
                .getRoot().getAbsolutePath(), 1000, Tangle.COLUMN_FAMILIES, Tangle.METADATA_COLUMN_FAMILY));
        provider1 = new LocalInMemoryGraphProvider("", tangle1);
        tangle1.addPersistenceProvider(provider1);
        tangle1.init();
    }

    @Test
    public void testGetSibling() throws Exception {
        TransactionViewModel a, b, c, d, e, f, g, h;
        a = new TransactionViewModel(getRandomTransactionTrits(), getRandomTransactionHash());
        b = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(a.getHash(),
                a.getHash()), getRandomTransactionHash());
        d = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(b.getHash(),
                b.getHash()), getRandomTransactionHash());
        c = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(b.getHash(),
                d.getHash()), getRandomTransactionHash());
        e = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(b.getHash(),
                d.getHash()), getRandomTransactionHash());
        f = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(b.getHash(),
                e.getHash()), getRandomTransactionHash());
        g = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(c.getHash(),
                d.getHash()), getRandomTransactionHash());
        h = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(e.getHash(),
                f.getHash()), getRandomTransactionHash());

        HashMap<Hash, String> tag = new HashMap<Hash, String>();
        tag.put(a.getHash(), "A");
        tag.put(b.getHash(), "B");
        tag.put(c.getHash(), "C");
        tag.put(d.getHash(), "D");
        tag.put(e.getHash(), "E");
        tag.put(f.getHash(), "F");
        tag.put(g.getHash(), "G");
        tag.put(h.getHash(), "H");
        provider1.setNameMap(tag);

        a.store(tangle1);
        b.store(tangle1);
        d.store(tangle1);
        c.store(tangle1);
        e.store(tangle1);
        f.store(tangle1);
        g.store(tangle1);
        h.store(tangle1);

        Hash[] hashes = {c.getHash(), d.getHash(), f.getHash()};
        Assert.assertTrue(CollectionUtils.containsAll(provider1.getSiblings(e.getHash()).stream().collect(Collectors.toList()), Arrays.asList(hashes)));
        // reset in memory graph
        provider1.close();
    }

    @Test
    public void testGetPivot() throws Exception {
        TransactionViewModel a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z, end1, end2;
        a = new TransactionViewModel(getRandomTransactionTrits(), getRandomTransactionHash());
        b = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(a.getHash(),
                a.getHash()), getRandomTransactionHash());
        c = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(a.getHash(),
                a.getHash()), getRandomTransactionHash());
        d = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(a.getHash(),
                a.getHash()), getRandomTransactionHash());
        e = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(a.getHash(),
                a.getHash()), getRandomTransactionHash());
        h = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(e.getHash(),
                d.getHash()), getRandomTransactionHash());
        f = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(d.getHash(),
                e.getHash()), getRandomTransactionHash());
        g = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(b.getHash(),
                c.getHash()), getRandomTransactionHash());
        i = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(g.getHash(),
                f.getHash()), getRandomTransactionHash());
        j = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(g.getHash(),
                f.getHash()), getRandomTransactionHash());
        m = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(j.getHash(),
                i.getHash()), getRandomTransactionHash());
        k = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(h.getHash(),
                j.getHash()), getRandomTransactionHash());
        l = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(i.getHash(),
                h.getHash()), getRandomTransactionHash());
        q = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(m.getHash(),
                k.getHash()), getRandomTransactionHash());
        s = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(k.getHash(),
                m.getHash()), getRandomTransactionHash());
        p = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(m.getHash(),
                k.getHash()), getRandomTransactionHash());
        n = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(l.getHash(),
                l.getHash()), getRandomTransactionHash());
        o = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(k.getHash(),
                l.getHash()), getRandomTransactionHash());
        r = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(k.getHash(),
                l.getHash()), getRandomTransactionHash());
        u = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(p.getHash(),
                n.getHash()), getRandomTransactionHash());
        v = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(n.getHash(),
                o.getHash()), getRandomTransactionHash());
        t = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(o.getHash(),
                n.getHash()), getRandomTransactionHash());
        w = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(q.getHash(),
                s.getHash()), getRandomTransactionHash());
        x = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(s.getHash(),
                u.getHash()), getRandomTransactionHash());
        y = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(t.getHash(),
                r.getHash()), getRandomTransactionHash());
        z = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(v.getHash(),
                t.getHash()), getRandomTransactionHash());
        end1 = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(x.getHash(),
                w.getHash()), getRandomTransactionHash());
        end2 = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(y.getHash(),
                z.getHash()), getRandomTransactionHash());

        TransactionViewModel[] models = {a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z, end1, end2};
        char[] modelChar = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r'
                , 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '1', '2'};
        HashMap<Hash, String> tag = new HashMap<Hash, String>();
        for (int offset = 0; offset < models.length; offset++) {
            tag.put(models[offset].getHash(), String.valueOf(modelChar[offset]));
        }
        provider1.setNameMap(tag);

        Arrays.stream(models).forEach(model -> {
            try {
                model.store(tangle1);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        System.out.println("=========pivot=======");
        assert provider1.getPivot(a.getHash()).equals(w.getHash()) || provider1.getPivot(a.getHash()).equals(u.getHash()) ;
        // reset in memory graph
        provider1.close();
    }

    @Test
    public void testPast() throws Exception {
        TransactionViewModel a, b, c, d, e, f, g, h;
        a = new TransactionViewModel(getRandomTransactionTrits(), getRandomTransactionHash());
        b = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(a.getHash(),
                a.getHash()), getRandomTransactionHash());
        c = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(a.getHash(),
                a.getHash()), getRandomTransactionHash());
        d = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(b.getHash(),
                b.getHash()), getRandomTransactionHash());
        e = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(b.getHash(),
                c.getHash()), getRandomTransactionHash());
        f = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(d.getHash(),
                e.getHash()), getRandomTransactionHash());
        g = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(e.getHash(),
                c.getHash()), getRandomTransactionHash());
        h = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(g.getHash(),
                f.getHash()), getRandomTransactionHash());

        HashMap<Hash, String> tag = new HashMap<Hash, String>();
        tag.put(a.getHash(), "A");
        tag.put(b.getHash(), "B");
        tag.put(c.getHash(), "C");
        tag.put(d.getHash(), "D");
        tag.put(e.getHash(), "E");
        tag.put(f.getHash(), "F");
        tag.put(g.getHash(), "G");
        tag.put(h.getHash(), "H");
        provider1.setNameMap(tag);

        a.store(tangle1);
        b.store(tangle1);
        d.store(tangle1);
        c.store(tangle1);
        e.store(tangle1);
        f.store(tangle1);
        g.store(tangle1);
        h.store(tangle1);

        System.out.println("============past============");
        Set<Hash> assertingSet = Arrays.stream(new Hash[]{a.getHash(), b.getHash(), c.getHash(), e.getHash(), g.getHash()}).collect(Collectors.toSet());
        assert assertingSet.removeAll(provider1.past(g.getHash())) && assertingSet.isEmpty();

        // reset in memory graph
        provider1.close();
    }

    @Test
    public void testGetPivotChain() throws Exception {
        TransactionViewModel a, b, c, d, e, f, g, h;
        a = new TransactionViewModel(getRandomTransactionTrits(), getRandomTransactionHash());
        b = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(a.getHash(),
                a.getHash()), getRandomTransactionHash());
        d = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(b.getHash(),
                b.getHash()), getRandomTransactionHash());
        c = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(b.getHash(),
                d.getHash()), getRandomTransactionHash());
        e = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(b.getHash(),
                d.getHash()), getRandomTransactionHash());
        f = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(b.getHash(),
                e.getHash()), getRandomTransactionHash());
        g = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(c.getHash(),
                d.getHash()), getRandomTransactionHash());
        h = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(e.getHash(),
                f.getHash()), getRandomTransactionHash());

        HashMap<Hash, String> tag = new HashMap<Hash, String>();
        tag.put(a.getHash(), "A");
        tag.put(b.getHash(), "B");
        tag.put(c.getHash(), "C");
        tag.put(d.getHash(), "D");
        tag.put(e.getHash(), "E");
        tag.put(f.getHash(), "F");
        tag.put(g.getHash(), "G");
        tag.put(h.getHash(), "H");
        provider1.setNameMap(tag);

        a.store(tangle1);
        b.store(tangle1);
        d.store(tangle1);
        c.store(tangle1);
        e.store(tangle1);
        f.store(tangle1);
        g.store(tangle1);
        h.store(tangle1);

        System.out.println("=========testGetPivotChain=======");
        List<Hash> assertingList1 = Arrays.stream(new Hash[]{a.getHash(), b.getHash(), e.getHash(), h.getHash()}).collect(Collectors.toList());
        List<Hash> assertingList2 = Arrays.stream(new Hash[]{a.getHash(), b.getHash(), c.getHash(), g.getHash()}).collect(Collectors.toList());
        List<Hash> rs = provider1.pivotChain(a.getHash());
        assert assertingList1.equals(rs) || assertingList2.equals(rs);
        // reset in memory graph
        provider1.close();
    }

    @Test
    public void testBuildSubGraph() throws Exception {
        TransactionViewModel a, b, c, d, e, f, g, h, i;
        a = new TransactionViewModel(getRandomTransactionTrits(), getRandomTransactionHash());
        i = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(a.getHash(),
                a.getHash()), getRandomTransactionHash());
        b = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(a.getHash(),
                a.getHash()), getRandomTransactionHash());
        d = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(b.getHash(),
                i.getHash()), getRandomTransactionHash());
        c = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(b.getHash(),
                d.getHash()), getRandomTransactionHash());
        e = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(b.getHash(),
                d.getHash()), getRandomTransactionHash());
        f = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(b.getHash(),
                e.getHash()), getRandomTransactionHash());
        g = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(c.getHash(),
                d.getHash()), getRandomTransactionHash());
        h = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(e.getHash(),
                f.getHash()), getRandomTransactionHash());

        HashMap<Hash, String> tag = new HashMap<Hash, String>();
        tag.put(a.getHash(), "A");
        tag.put(b.getHash(), "B");
        tag.put(c.getHash(), "C");
        tag.put(d.getHash(), "D");
        tag.put(e.getHash(), "E");
        tag.put(f.getHash(), "F");
        tag.put(g.getHash(), "G");
        tag.put(h.getHash(), "H");
        tag.put(i.getHash(), "M");
        provider1.setNameMap(tag);

        a.store(tangle1);
        i.store(tangle1);
        b.store(tangle1);
        d.store(tangle1);
        c.store(tangle1);
        e.store(tangle1);
        f.store(tangle1);
        g.store(tangle1);
        h.store(tangle1);

        System.out.println("============buildSubGraph============");
        List<Hash> blocks = Arrays.stream(new Hash[]{b.getHash(), c.getHash(), d.getHash()}).collect(Collectors.toList());
        Map<Hash, Set<Hash>> subGraph = provider1.buildSubGraph(blocks);
        Map<Hash,Set<Hash>> assertingMap = new HashMap(){{
            put(b.getHash(), Collections.emptySet());
            put(d.getHash(), Arrays.stream(new Hash[]{b.getHash()}).collect(Collectors.toSet()));
            put(c.getHash(), Arrays.stream(new Hash[]{b.getHash(), d.getHash()}).collect(Collectors.toSet()));
        }};
        assert  assertingMap.equals(subGraph);

        // reset in memory graph
        provider1.close();
    }

    @Test
    public void testConfluxOrder() throws Exception {
        TransactionViewModel a, b, c, d, e, f, g, h, i;
        a = new TransactionViewModel(getRandomTransactionTrits(), getRandomTransactionHash());
        i = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(a.getHash(),
                a.getHash()), getRandomTransactionHash());
        b = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(a.getHash(),
                a.getHash()), getRandomTransactionHash());
        d = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(b.getHash(),
                i.getHash()), getRandomTransactionHash());
        c = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(b.getHash(),
                d.getHash()), getRandomTransactionHash());
        e = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(b.getHash(),
                d.getHash()), getRandomTransactionHash());
        f = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(b.getHash(),
                e.getHash()), getRandomTransactionHash());
        g = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(c.getHash(),
                d.getHash()), getRandomTransactionHash());
        h = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(e.getHash(),
                f.getHash()), getRandomTransactionHash());

        HashMap<Hash, String> tag = new HashMap<Hash, String>();
        tag.put(a.getHash(), "A");
        tag.put(b.getHash(), "B");
        tag.put(c.getHash(), "C");
        tag.put(d.getHash(), "D");
        tag.put(e.getHash(), "E");
        tag.put(f.getHash(), "F");
        tag.put(g.getHash(), "G");
        tag.put(h.getHash(), "H");
        tag.put(i.getHash(), "M");
        provider1.setNameMap(tag);

        a.store(tangle1);
        i.store(tangle1);
        b.store(tangle1);
        d.store(tangle1);
        c.store(tangle1);
        e.store(tangle1);
        f.store(tangle1);
        g.store(tangle1);
        h.store(tangle1);

        System.out.println("============confluxOrder============");
        List<Hash> assertingList = Arrays.stream(new Hash[]{a.getHash(), b.getHash(), i.getHash(), d.getHash()}).collect(Collectors.toList());
        assert  assertingList.equals(provider1.confluxOrder(d.getHash()));

        // reset in memory graph
        provider1.close();
    }

    @Test
    public void testTotalTopOrder() throws Exception {
        TransactionViewModel a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z, end1, end2;
        a = new TransactionViewModel(getRandomTransactionTrits(), getRandomTransactionHash());
        b = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(a.getHash(),
                a.getHash()), getRandomTransactionHash());
        c = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(a.getHash(),
                a.getHash()), getRandomTransactionHash());
        d = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(a.getHash(),
                a.getHash()), getRandomTransactionHash());
        e = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(a.getHash(),
                a.getHash()), getRandomTransactionHash());
        h = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(e.getHash(),
                d.getHash()), getRandomTransactionHash());
        f = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(d.getHash(),
                e.getHash()), getRandomTransactionHash());
        g = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(b.getHash(),
                c.getHash()), getRandomTransactionHash());
        i = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(g.getHash(),
                f.getHash()), getRandomTransactionHash());
        j = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(g.getHash(),
                f.getHash()), getRandomTransactionHash());
        m = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(j.getHash(),
                i.getHash()), getRandomTransactionHash());
        k = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(h.getHash(),
                j.getHash()), getRandomTransactionHash());
        l = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(i.getHash(),
                h.getHash()), getRandomTransactionHash());
        q = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(m.getHash(),
                k.getHash()), getRandomTransactionHash());
        s = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(k.getHash(),
                m.getHash()), getRandomTransactionHash());
        p = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(m.getHash(),
                k.getHash()), getRandomTransactionHash());
        n = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(m.getHash(),
                k.getHash()), getRandomTransactionHash());
        o = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(k.getHash(),
                l.getHash()), getRandomTransactionHash());
        r = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(k.getHash(),
                l.getHash()), getRandomTransactionHash());
        u = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(p.getHash(),
                n.getHash()), getRandomTransactionHash());
        v = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(n.getHash(),
                o.getHash()), getRandomTransactionHash());
        t = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(o.getHash(),
                n.getHash()), getRandomTransactionHash());
        w = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(q.getHash(),
                s.getHash()), getRandomTransactionHash());
        x = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(s.getHash(),
                u.getHash()), getRandomTransactionHash());
        y = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(t.getHash(),
                r.getHash()), getRandomTransactionHash());
        z = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(v.getHash(),
                t.getHash()), getRandomTransactionHash());
        end1 = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(x.getHash(),
                w.getHash()), getRandomTransactionHash());
        end2 = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(y.getHash(),
                z.getHash()), getRandomTransactionHash());

        TransactionViewModel[] models = {a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z, end1, end2};
        char[] modelChar = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r'
                , 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '1', '2'};
        HashMap<Hash, String> tag = new HashMap<Hash, String>();
        for (int offset = 0; offset < models.length; offset++) {
            tag.put(models[offset].getHash(), String.valueOf(modelChar[offset]));
        }
        provider1.setNameMap(tag);

        Arrays.stream(models).forEach(model -> {
            try {
                model.store(tangle1);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        System.out.println("=============total order=======");
        List<Hash> assertingList1 = Arrays.stream(new Hash[]{a.getHash(),b.getHash(),c.getHash(),g.getHash(),e.getHash(),d.getHash(),f.getHash(),
                j.getHash(),i.getHash(),m.getHash(),h.getHash(),k.getHash(),n.getHash(),l.getHash(),o.getHash(),
                v.getHash(),t.getHash(),z.getHash()}).collect(Collectors.toList());
        List<Hash> assertingList2 = Arrays.stream(new Hash[]{a.getHash(),d.getHash(),e.getHash(),f.getHash()}).collect(Collectors.toList());

        List<Hash> rs = provider1.totalTopOrder();
        // because the score is unfixed
        assert assertingList1.removeAll(rs) && assertingList1.isEmpty() || assertingList2.equals(rs);

        // reset in memory graph
        provider1.close();
    }

    @Test
    public void testGetDiffSet() throws Exception {
        TransactionViewModel a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z, end1, end2;
        a = new TransactionViewModel(getRandomTransactionTrits(), getRandomTransactionHash());
        b = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(a.getHash(),
                a.getHash()), getRandomTransactionHash());
        c = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(a.getHash(),
                a.getHash()), getRandomTransactionHash());
        d = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(a.getHash(),
                a.getHash()), getRandomTransactionHash());
        e = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(a.getHash(),
                a.getHash()), getRandomTransactionHash());
        h = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(e.getHash(),
                d.getHash()), getRandomTransactionHash());
        f = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(d.getHash(),
                e.getHash()), getRandomTransactionHash());
        g = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(b.getHash(),
                c.getHash()), getRandomTransactionHash());
        i = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(g.getHash(),
                f.getHash()), getRandomTransactionHash());
        j = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(g.getHash(),
                f.getHash()), getRandomTransactionHash());
        m = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(j.getHash(),
                i.getHash()), getRandomTransactionHash());
        k = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(h.getHash(),
                j.getHash()), getRandomTransactionHash());
        l = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(i.getHash(),
                h.getHash()), getRandomTransactionHash());
        q = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(m.getHash(),
                k.getHash()), getRandomTransactionHash());
        s = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(k.getHash(),
                m.getHash()), getRandomTransactionHash());
        p = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(m.getHash(),
                k.getHash()), getRandomTransactionHash());
        n = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(m.getHash(),
                k.getHash()), getRandomTransactionHash());
        o = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(k.getHash(),
                l.getHash()), getRandomTransactionHash());
        r = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(k.getHash(),
                l.getHash()), getRandomTransactionHash());
        u = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(p.getHash(),
                n.getHash()), getRandomTransactionHash());
        v = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(n.getHash(),
                o.getHash()), getRandomTransactionHash());
        t = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(o.getHash(),
                n.getHash()), getRandomTransactionHash());
        w = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(q.getHash(),
                s.getHash()), getRandomTransactionHash());
        x = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(s.getHash(),
                u.getHash()), getRandomTransactionHash());
        y = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(t.getHash(),
                r.getHash()), getRandomTransactionHash());
        z = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(v.getHash(),
                t.getHash()), getRandomTransactionHash());
        end1 = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(x.getHash(),
                w.getHash()), getRandomTransactionHash());
        end2 = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(y.getHash(),
                z.getHash()), getRandomTransactionHash());

        TransactionViewModel[] models = {a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z, end1, end2};
        char[] modelChar = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r'
                , 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '1', '2'};
        HashMap<Hash, String> tag = new HashMap<Hash, String>();
        for (int offset = 0; offset < models.length; offset++) {
            tag.put(models[offset].getHash(), String.valueOf(modelChar[offset]));
        }
        provider1.setNameMap(tag);

        Arrays.stream(models).forEach(model -> {
            try {
                model.store(tangle1);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        System.out.println("=============getDiffSet=======");
        List<Hash> assertingList = Arrays.stream(new Hash[]{y.getHash(),r.getHash()}).collect(Collectors.toList());

        //total graph
//        localInMemoryGraphProvider.printGraph(localInMemoryGraphProvider.graph,null);

        List<Hash> rs = provider1.getDiffSet(y.getHash(), t.getHash(), new HashSet<>());
        CollectionUtils.emptyIfNull(rs).forEach(ha -> System.out.println(tag.get(ha)));
        assert CollectionUtils.isEqualCollection(assertingList, rs);

        // reset in memory graph
        provider1.close();
    }

    // Genesis前推测试
    @Test
    public void testInitWithGenesisForward() throws Exception {
        // start genesis forward engine。 Firstly, engine run parameters should be change immediately
        tangle1.shutdown();
        TestnetConfig iotaConfig = (TestnetConfig) BaseIotaConfig.getInstance();
        iotaConfig.setAncestorForwardEnable(true);
        iotaConfig.setAncestorCreateFrequency(1.0);
        iotaConfig.setAncestorForwardPeriod(1);
        tangle1 = new Tangle();
        provider1.init();
    }

    // build graph will get block from tangle and build graph in memory
    @Test
    public void testBuildGraph(){
        // add block
        HashMap<Hash, String> tagNameMap = new HashMap<>();
        mockGraph(tangle1, tagNameMap);
        List<Hash> totalOrder = provider1.totalTopOrder();
        provider1.buildGraph();
        List<Hash> totalOrderAfterBuild = provider1.totalTopOrder();
        Assert.assertEquals(totalOrder, totalOrderAfterBuild);
    }

    @Test
    public void testComputeScore(){
        HashMap<Hash, String> tagNameMap = new HashMap<>();
        mockGraph(tangle1, tagNameMap);
        Hash anyBlock = tagNameMap.keySet().iterator().next();
        double score1 = provider1.getScore(anyBlock);
        provider1.computeScore();
        double score2 = provider1.getScore(anyBlock);
        Assert.assertEquals(score1, score2, 2);
    }

    @Test
    public void testComputeScoreUseKATZ() throws Exception {
        tangle1.shutdown();
        tangle1 = new Tangle();
        tangle1.addPersistenceProvider(new RocksDBPersistenceProvider(dbFolder.getRoot().getAbsolutePath(), logFolder
                .getRoot().getAbsolutePath(), 1000, Tangle.COLUMN_FAMILIES, Tangle.METADATA_COLUMN_FAMILY));
        provider1 = new LocalInMemoryGraphProvider("", tangle1);
        tangle1.addPersistenceProvider(provider1);
        TestnetConfig config = (TestnetConfig) BaseIotaConfig.getInstance();
        config.setConfluxScoreAlgo("KATZ");
        tangle1.init();

        HashMap<Hash, String> tagNameMap = new HashMap<>();
        mockGraph(tangle1, tagNameMap);
        Hash anyBlock = tagNameMap.keySet().iterator().next();
        double score1 = provider1.getScore(anyBlock);
        provider1.computeScore();
        double score2 = provider1.getScore(anyBlock);
        Assert.assertEquals(score1, score2, 2);
    }

    // todo 未覆盖完全
    @Test
    public void testGetPivotalHash(){
        HashMap<Hash, String> tagNameMap = new HashMap<>();
        mockGraph(tangle1, tagNameMap);
        Hash hash = provider1.getPivotalHash(1);
        Hash vHash = tagNameMap.entrySet().stream().filter(e -> e.getValue().equals("v")).map(Map.Entry::getKey).collect(Collectors.toList()).get(0);
        Assert.assertEquals(hash, vHash);
    }

    @Test
    public void testPrintGraph(){
        HashMap<Hash, String> tagNameMap = new HashMap<>();
        mockGraph(tangle1, tagNameMap);
        provider1.setNameMap(tagNameMap);
        Map<Hash, Set<Hash>> graph = provider1.getGraph();
        String graphStr = provider1.printGraph(graph, "DOT");
        System.out.println(graphStr);
        graphStr = provider1.printGraph(graph, "JSON");
        System.out.println(graphStr);
    }

    @Test
    public void testPringOrder(){
        List<Hash> hashes = new ArrayList<>();
        for (int i=0; i < 10; i++){
            hashes.add(IotaUtils.getRandomTransactionHash());
        }
        String s = provider1.printOrder(hashes);
        System.out.println(s);
    }

    @Test
    public void testGetSiblingsWithAbsentBlock(){
        HashMap<Hash, String> tagNameMap = new HashMap<>();
        mockGraph(tangle1, tagNameMap);
        List<Hash> hashes = provider1.getSiblings(IotaUtils.getRandomTransactionHash());
        Assert.assertTrue(hashes.isEmpty());
    }

    /**
     *  example :
     *      g(1) - e(0)
     *      /     /
     *    /      b(2)
     *   /    /  \
     *  a(3)    f(0)
     *      \
     *       c(0)
     *  like the graph, if choose the block e, the chain would be (a - b - e)
     *  note: test case not same as the example before.
     */
    @Test
    public void testGetChain(){
        HashMap<Hash, String> tagNameMap = new HashMap<>();
        mockGraph(tangle1, tagNameMap);

        HashMap<Integer, Set<Hash>> topOrder = new HashMap<>();
        Set<Hash> set = new HashSet<>();
        Hash vHash = tagNameMap.entrySet().stream().filter(e -> e.getValue().equals("v")).map(Map.Entry::getKey).collect(Collectors.toList()).get(0);
        set.add(vHash);
        topOrder.put(1, set);

        List<Hash> chain = provider1.getChain(topOrder);
        chain.forEach(c -> System.out.println(tagNameMap.get(c)));
    }

    /**
     * a tip means the latest block appended to graph
     */
    @Test
    public void testGetNumOfTips(){
        HashMap<Hash, String> tagNameMap = new HashMap<>();
        mockGraph(tangle1, tagNameMap);

        int numOfTips = provider1.getNumOfTips();
        Assert.assertTrue(2 == numOfTips);
    }

    /**
     * unused, just print it
     */
    @Test
    public void testGetCondensedGraph(){
        HashMap<Hash, String> tagNameMap = new HashMap<>();
        mockGraph(tangle1, tagNameMap);

        System.out.println(provider1.getCondensedGraph());
    }

    @Test
    public void testGetHashedFromBundle(){
        HashMap<Hash, String> tagNameMap = new HashMap<>();
        mockGraph(tangle1, tagNameMap);

        Hash hash = tagNameMap.keySet().iterator().next();
        List<String> bundleHashes = Arrays.asList(hash.toString());
        List<Hash> hashes = provider1.getHashesFromBundle(bundleHashes);
        Assert.assertTrue(hashes.size() == 1 && hashes.get(0).equals(hash));
    }

    /**
     * a sub graph with given genesis and blocks
     */
    @Test
    public void testBuildTempGraphs(){
        HashMap<Hash, String> tagNameMap = new HashMap<>();
        mockGraph(tangle1, tagNameMap);

        Hash genesis = tagNameMap.entrySet().stream().filter(e -> e.getValue().equals("a")).map(Map.Entry::getKey).collect(Collectors.toList()).get(0);
        List<Hash> allBlocks = new ArrayList<>(tagNameMap.keySet());
        allBlocks.remove(genesis);

        provider1.buildTempGraphs(allBlocks, genesis);
    }

    @Test
    public void testReverseTmpGraph(){
        HashMap<Hash, String> tagNameMap = new HashMap<>();
        mockGraph(tangle1, tagNameMap);

        Hash genesis = tagNameMap.entrySet().stream().filter(e -> e.getValue().equals("a")).map(Map.Entry::getKey).collect(Collectors.toList()).get(0);
        List<Hash> allBlocks = new ArrayList<>(tagNameMap.keySet());
        allBlocks.remove(genesis);

        provider1.reserveTempGraphs(allBlocks, genesis);
    }

    // todo add assert
    @Test
    public void testShiftTempGraph(){
        HashMap<Hash, String> tagNameMap = new HashMap<>();
        mockGraph(tangle1, tagNameMap);

        provider1.shiftTempGraphs();
    }

    // todo assert
    @Test
    public void testInduceGraphFromAncestor() throws Exception {
        HashMap<Hash, String> tagNameMap = new HashMap<>();
        mockGraph(tangle1, tagNameMap);

        Hash genesis = tagNameMap.entrySet().stream().filter(e -> e.getValue().equals("a")).map(Map.Entry::getKey).collect(Collectors.toList()).get(0);
        provider1.induceGraphFromAncestor(genesis);
    }

    @Test
    public void testGenesisForwardWithInnerClass() throws Exception {
        tangle1.shutdown();
        dbFolder.delete();
        dbFolder1.delete();

        BaseIotaConfig iotaConfig = BaseIotaConfig.getInstance();
        iotaConfig.setAncestorForwardEnable(true);
        iotaConfig.setAncestorCreateFrequency(1.0);
        iotaConfig.setAncestorForwardPeriod(1); // time to last forward

        tangle1 = new Tangle();
        tangle1.addPersistenceProvider(new RocksDBPersistenceProvider(dbFolder.getRoot().getAbsolutePath(), logFolder
                .getRoot().getAbsolutePath(), 1000, Tangle.COLUMN_FAMILIES, Tangle.METADATA_COLUMN_FAMILY));
        tangle1.addPersistenceProvider(provider1);
        tangle1.init();

        HashMap<Hash, String> tagNameMap = new HashMap<>();
        mockGraph(tangle1, tagNameMap);

        Thread.sleep(12000);

        Stack<Hash> historyGenesises = provider1.getAncestors();
        System.out.println(historyGenesises);
    }

    //mock graph
    private void mockGraph(Tangle tangle, HashMap<Hash, String> tagNameMap){
        TransactionViewModel a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z, end1, end2;
        a = new TransactionViewModel(getRandomTransactionTrits(), getRandomTransactionHash());
        b = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(a.getHash(),
                a.getHash()), getRandomTransactionHash());
        c = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(a.getHash(),
                a.getHash()), getRandomTransactionHash());
        d = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(a.getHash(),
                a.getHash()), getRandomTransactionHash());
        e = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(a.getHash(),
                a.getHash()), getRandomTransactionHash());
        h = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(e.getHash(),
                d.getHash()), getRandomTransactionHash());
        f = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(d.getHash(),
                e.getHash()), getRandomTransactionHash());
        g = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(b.getHash(),
                c.getHash()), getRandomTransactionHash());
        i = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(g.getHash(),
                f.getHash()), getRandomTransactionHash());
        j = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(g.getHash(),
                f.getHash()), getRandomTransactionHash());
        m = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(j.getHash(),
                i.getHash()), getRandomTransactionHash());
        k = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(h.getHash(),
                j.getHash()), getRandomTransactionHash());
        l = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(i.getHash(),
                h.getHash()), getRandomTransactionHash());
        q = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(m.getHash(),
                k.getHash()), getRandomTransactionHash());
        s = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(k.getHash(),
                m.getHash()), getRandomTransactionHash());
        p = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(m.getHash(),
                k.getHash()), getRandomTransactionHash());
        n = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(m.getHash(),
                k.getHash()), getRandomTransactionHash());
        o = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(k.getHash(),
                l.getHash()), getRandomTransactionHash());
        r = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(k.getHash(),
                l.getHash()), getRandomTransactionHash());
        u = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(p.getHash(),
                n.getHash()), getRandomTransactionHash());
        v = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(n.getHash(),
                o.getHash()), getRandomTransactionHash());
        t = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(o.getHash(),
                n.getHash()), getRandomTransactionHash());
        w = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(q.getHash(),
                s.getHash()), getRandomTransactionHash());
        x = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(s.getHash(),
                u.getHash()), getRandomTransactionHash());
        y = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(t.getHash(),
                r.getHash()), getRandomTransactionHash());
        z = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(v.getHash(),
                t.getHash()), getRandomTransactionHash());
        end1 = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(x.getHash(),
                w.getHash()), getRandomTransactionHash());
        end2 = new TransactionViewModel(getRandomTransactionWithTrunkAndBranch(y.getHash(),
                z.getHash()), getRandomTransactionHash());

        TransactionViewModel[] models = {a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z, end1, end2};
        char[] modelChar = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r'
                , 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '1', '2'};

        for (int offset = 0; offset < models.length; offset++) {
            tagNameMap.put(models[offset].getHash(), String.valueOf(modelChar[offset]));
        }

        Arrays.stream(models).forEach(model -> {
            try {
                model.store(tangle);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

}
