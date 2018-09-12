# Worker

The ones who actually do the heavy load.

Workers have an `_fs/inbox` to which messages are written. 
They report back to the broker by writing to the brokers `_fs/inbox`

Workers can make use of their `_fs/work` directory if they require 
local storage or temporary files.
