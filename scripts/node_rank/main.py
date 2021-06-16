import yaml
import requests
import random
import _thread as thread
import threading
import time
import json
import pandas as pd
from datetime import datetime

from pandas import DataFrame

rankResult: DataFrame = pd.DataFrame(columns=('round', 'address', 'node', 'score', 'timestamp'))


def post(url, data):
    """
    post method
    :param url:
    :param data:
    :param headers:
    :return:
    """
    headers = {
        'Content-Type': 'application/json',
        'cache-control': 'no-cache'
    }
    return requests.post(url=url, data=json.dumps(data), headers=headers)


def add_data(url, attestee, attester, score):
    """
    add node transaction
    :return: None
    """
    data = {
        'Attester': attestee,
        'Attestee': attester,
        'Score': score
    }
    res = post(url=url, data=data)
    if res.status_code != 200:
        print("request url : {} failed, state_code: {}".format(url, res.status_code))
    print("add {} success! ".format(json.dumps(data)))


def query_node(address, round):
    """
    query node result fragment
    [[address, score]]
    :return:
    """

    url = 'http://{}:8000/QueryNodes'.format(address)

    data = {
        'period': 1,
        'numRank': 100
    }
    now = datetime.now().timestamp()
    global rankResult

    try:
        res = post(url=url, data=data)
        if res.status_code != 200:
            msg = 'query node rank failed, response status: {}'.format(res.status_code)
            print(msg)
            raise Exception(msg)

        res_json = json.loads(res.text)
        if res_json['code'] != 0:
            msg = 'query node rank failed, code: {}, message: {}'.format(res_json['code'], res_json['message'])
            print(msg)
            raise Exception(msg)

        rank_rst = res_json['data']
        if None == rank_rst or rank_rst == {}:
            msg = 'query node rank failed, response contains no data'
            print(msg)
            raise Exception(msg)

        data_score = rank_rst['dataScore']
        if None == data_score or [] == data_score:
            msg = 'query node rank failed, response contains no rank score'
            print(msg)
            raise Exception(msg)

        for node_score in data_score:
            rankResult = rankResult.append({
                'round': round,
                'address': address,
                'node': node_score['attestee'],
                'score': node_score['score'],
                'timestamp': now
            }, ignore_index=True)
    except Exception as e:
        print(" error !", e)
        rankResult = rankResult.append({
            'round': round,
            'address': address,
            'node': 'none',
            'score': 0,
            'timestamp': now
        }, ignore_index=True)


def compare_result(conf):
    """
    iterate node result
    相同数量、查询序号、时间戳
    不同数量、查询序号、时间戳
    生成散点图
    :return:
    """
    query_round = 0
    while query_round < conf['round']:
        time.sleep(1)
        query_round = query_round + 1
        # fetch data from all node
        # threads = []
        for host in conf['host']:
            query_node(host['address'], query_round)
            # th = threading.Thread(target=query_node, args=(host['address'], query_round))
            # threads.append(th)
            # th.start()
        # for cur_thread in threads:
        #     cur_thread.join()


def generate_address(count):
    if count < 0:
        return []
    addrs = []
    for i in range(count):
        addrs.append('192.168.1.{}'.format(2 + i))
    return addrs


def generate_address_pairs(addresses):
    if len(addresses) < 1:
        return

    if len(addresses) < 2:
        return {
            'left': addresses[0],
            'right': addresses[0]
        }

    left_idx = random.randint(0, len(addresses) - 1)
    while True:
        right_idx = random.randint(0, len(addresses) - 1)
        if right_idx != left_idx:
            break

    return {
        'left': addresses[left_idx],
        'right': addresses[right_idx]
    }


def dataProducer(conf):
    """
    随机生成1-10的分数，生成指定的节点数
    :param conf:
    :return:
    """
    len_host = len(conf['host'])
    addresses = generate_address(conf['nodeNum'])
    count = conf['count']
    while count > 0:
        score = random.randint(1, 10)
        pair = generate_address_pairs(addresses)

        idx = random.randint(0, len_host - 1)
        host = conf['host'][idx]
        url = 'http://{}:{}/AddNode'.format(host['address'], host['port'])
        add_data(url, pair['left'], pair['right'], score)
        count = count - 1
        time.sleep(0.1)


def main():
    f = open('env.yaml')
    conf = yaml.load(f, Loader=yaml.FullLoader)
    if conf['onlyWrite'] == True:
        producer_thread = threading.Thread(target=dataProducer, args=(conf, ))
        producer_thread.start()
    if conf['onlyRead'] != True:
        # query Node
        compare_result(conf)
        # producer_thread.join()
        rankResult.to_csv('data.csv')
    df = pd.read_csv('data.csv')
    # print(df)
    # get score std group by node per round
    df1 = df.groupby(['round','node']).agg({'score':'std'})
    # get diff by round
    df2 = df1.groupby('round').agg({'score':'sum'})
    # print(df2)
    # get total diff
    df3 = df2.agg({'score': 'sum'})
    print(df3)



if __name__ == '__main__':
    main()

