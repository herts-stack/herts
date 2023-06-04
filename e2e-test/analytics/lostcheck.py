import pandas as pd

if __name__ == "__main__":
    sent_df = pd.read_csv('local/sent_msg.csv')
    print('## Sent message')
    print(sent_df.shape[0])
    print()

    received_df = pd.read_csv('local/received_msg.csv')
    print('## Received message')
    print(received_df.shape[0])
    print()

    merged_df = pd.merge(sent_df, received_df, on='message_id')
    print('## Merged message')
    print(merged_df.shape[0])
    print()
    print(merged_df.groupby('client_id')['client_id'].nunique())
    print()
