import asyncio
from aiohttp_sse_client import client as sse_client

async def listen_sse(url, jwt, role):
    headers = {"Authorization": f"Bearer {jwt}"}
    async with sse_client.EventSource(url, headers=headers) as event_source:
        async for event in event_source:
            print(f"[{role}] : {event.data}")

def main():
    url = "http://localhost:8086/notifications/stream"
    role = input("Enter the role: ")
    jwt = input("Enter the Jwt Token: ")
    asyncio.run(listen_sse(url, jwt, role))

if __name__ == "__main__":
    main()



